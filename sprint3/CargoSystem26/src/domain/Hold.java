package domain;

import java.util.Map;
import java.util.Objects;

import utils.HoldManifestReader;

public class Hold implements IHold{
	
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
	    
	    @Override
	    public String toString() {
	        return "(" + x + "," + y + ")";
	    }
	}
	
	// Usiamo una LinkedHashMap per mantenere l'ordine alfabeti	
	private int D;        //dimensione unità robotica in metri
	private int width;    // larghezza hold (in unità robotiche)
	private int length;   // lunghezza hold (in unità robotiche)
	private int[][] grid;         // griglia logica per la mappa mentaleco/inserimento degli slot

    private final Map<String, Coord> coords; // ioport, slot1-4, slot5
    

    public Hold(String manifestPath) {
    	int[] dims = HoldManifestReader.extractDimensions(HoldManifestReader.read(manifestPath));
        this.width = dims[0];
        this.length = dims[1];
        this.D = dims[2];
        this.coords = HoldManifestReader.extractCoordinates(
                HoldManifestReader.read(manifestPath));
    }

    public Coord coordinatesOf(String slotName) {
        Coord c = coords.get(slotName);
        if (c == null) throw new IllegalArgumentException("Unknown slot: " + slotName);
        return c;
    }
    
    public int getWidth() {
		return this.width;
	}

	public int getLength() {
		return this.length;
	}

	public int getD() {
		return this.D;
	}
}