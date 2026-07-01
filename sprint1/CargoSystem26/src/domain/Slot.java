package domain;

public class Slot {
	private String name;
	private boolean occupato = false;
	private Integer pid = null;
	
	public Slot(String name) {
		this.name = name;
	}
	
	public boolean isOccupato() {
		return occupato;
	}
	
	public void putContainer(Integer pid) {
		this.pid = pid;
		this.occupato = true;
	}
	
	public void removeContainer() {
		this.pid = null;
		this.occupato = false;
	}
}