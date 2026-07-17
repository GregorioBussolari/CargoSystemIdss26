package domain;

public interface ISlot {
	 //primitive
	 public String getName();         //Nome slot
	 public boolean isOccupato();      // stato corrente
	 //non primitive
	 public void putContainer();      // Inserisce container nello slot
	 public void removeContainer();   // Rimuove container dallo slot
}
