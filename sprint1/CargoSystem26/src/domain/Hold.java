package domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import cli.System.IO.IOException;

public class Hold implements IHold{
	private int D;        //dimensione unità robotica in metri
private int width;    // larghezza hold (in unità robotiche)
private int length;   // lunghezza hold (in unità robotiche)
private int[][] grid;         // griglia logica per la mappa mentale

private Map<Coord, Slot> gridSlots = new HashMap<>();

private Coord ioport;
private Coord slot5;

public static class Coord {
    private final int x;
    private final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

// Costruttore di base
	public Hold(int width, int length, int D) {
	    this.width = width;
	    this.length = length;
	    this.D = D;
	}
	
	public void addSlot(Coord coord, Slot slot) {
	    this.gridSlots.put(coord, slot);
	}
	
	public void clearAllSlots() {
	    // Scorriamo solo i valori (gli Slot) della mappa e li liberiamo
	    gridSlots.values().forEach(Slot::removeContainer);
	}

	public boolean isFull() {
			if (slot5 == null || gridSlots.isEmpty()) {
				return false;
		}
		
		// Logica per verificare se gli Slot 1-4 sono tutti occupati
		return gridSlots.entrySet().stream()
			        .filter(e -> !e.getKey().equals(slot5))
					    .allMatch(e -> e.getValue().isOccupato());
	}
	
	@Override
	public Coord getIoPort() {
		return ioport;
	}

	public void setIoport(Coord ioport) {
		this.ioport = ioport;
	}
	
	@Override
	public Coord getSlot5() {
		return slot5;
	}

	public void setSlot5(Coord slot5) {
		this.slot5 = slot5;
	}

	@Override
	public ISlot findFreeSlot() {
	    if (gridSlots.isEmpty()) {
	        return null;
	    }

	    return gridSlots.entrySet().stream()
	            // 1. Escludiamo lo slot5
	            .filter(e -> !e.getKey().equals(slot5))
	            // 2. Prendiamo il valore (lo Slot)
	            .map(Map.Entry::getValue)
	            // 3. Filtriamo per trovare quelli NON occupati
	            .filter(slot -> !slot.isOccupato())
	            // 4. ORDIDIAMO in base al nome dello slot per essere deterministici
	            .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
	            // 5. Ora "findFirst" prenderà DAVVERO il primo in ordine logico
	            .findFirst()
	            .orElse(null);
	}

	@Override
	public Coord getSlotCoord(ISlot slot) {
		for (Map.Entry<Coord, Slot> entry : gridSlots.entrySet()) {
	        if (Objects.equals(entry.getValue(), slot)) {
	            return entry.getKey();
	        }
	    }
	    return null; // Ritorna null se non trovato
	}

	
	
	public static Hold fromConfigFile(String filePath) throws IOException, FileNotFoundException, java.io.IOException {
	    Properties prop = new Properties();
	    
	    // Leggiamo il file di configurazione
	    try (InputStream input = new FileInputStream(filePath)) {
	        prop.load(input);
	    }

	    // 1. Leggiamo i dati strutturali della griglia
	    int width = Integer.parseInt(prop.getProperty("hold.width", "10"));
	    int length = Integer.parseInt(prop.getProperty("hold.length", "10"));
	    int d = Integer.parseInt(prop.getProperty("hold.D", "1"));

	    // Creiamo l'istanza reale di Hold
	    Hold hold = new Hold(width, length, d);

	    // 2. Leggiamo e configuriamo le coordinate fisse di sistema
	    hold.ioport = parseCoord(prop.getProperty("coord.ioport"));
	    hold.slot5 = parseCoord(prop.getProperty("coord.slot5"));

	    if (hold.ioport == null || hold.slot5 == null) {
	        throw new IllegalArgumentException("Errore di configurazione: 'coord.ioport' e 'coord.slot5' sono obbligatori.");
	    }

	    // 3. Inizializziamo e mappiamo gli Slot dinamici standard da 1 a 4
	    for (int i = 1; i <= 4; i++) {
	        String coordStr = prop.getProperty("coord.slot" + i);
	        if (coordStr != null) {
	            Coord slotCoord = parseCoord(coordStr);
	            // Inseriamo lo slot direttamente nella mappa gridSlots dell'istanza appena creata
	            hold.gridSlots.put(slotCoord, new Slot("Slot" + i));
	        }
	    }
	    
	    // Inseriamo anche lo slot5 speciale nella mappa per coerenza di calcolo
	    hold.gridSlots.put(hold.slot5, new Slot("Slot5"));

	    return hold;
	}

	/**
	 * Helper interno per convertire una stringa del tipo "x,y" in un oggetto Coord.
	 */
	private static Coord parseCoord(String rawValue) {
	    if (rawValue == null || rawValue.trim().isEmpty()) {
	        return null;
	    }
	    String[] parts = rawValue.split(",");
	    int x = Integer.parseInt(parts[0].trim());
	    int y = Integer.parseInt(parts[1].trim());
	    return new Coord(x, y);
	}


}
