package domain;

public class Slot implements ISlot{
	private String name;
	private boolean occupato;
	//private Integer pid = null;
	
	public Slot(String name) {
		this.name = name;
		this.occupato = false;
	}
	
	public boolean isOccupato() {
		return occupato;
	}
	
//	public void putContainer(Integer pid) {
//		this.pid = pid;
//		this.occupato = true;
//	}
	
//	public void removeContainer() {
//		this.pid = null;
//		this.occupato = false;
//	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void putContainer() {
		this.occupato = true;
	}
	
	public void removeContainer() {
		this.occupato = false;
	}
}