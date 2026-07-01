package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Hold {
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

	public boolean isFull() {
		// Logica per verificare se gli Slot 1-4 sono tutti occupati
	    return gridSlots.entrySet().stream()
				        .filter(e -> !e.getKey().equals(slot5))
						    .allMatch(e -> e.getValue().isOccupato());
    }
    
    
}
}
