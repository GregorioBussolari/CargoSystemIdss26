package domain;

public interface IHold {
	 //primtive 
  boolean isFull();                 //True se la hold non ha più slot liberi
  ISlot findFreeSlot();              //Restituisce uno slot libero o null nel caso non ci siano
  //non primitive
  Hold.Coord getSlotCoord(ISlot slot);         //Restituisce posizione di un dato slot
  Hold.Coord getSlot5();                 //Restituisce posizione slot 5
  Hold.Coord getIoPort();        //Restituisce posizione IOPort
}
