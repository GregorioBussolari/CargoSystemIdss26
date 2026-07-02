package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
	public Hold(int width, int length) {
	    this.width = width;
	    this.length = length;
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

}
