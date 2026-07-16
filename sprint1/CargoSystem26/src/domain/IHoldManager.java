package domain;


public interface IHoldManager {
	 //primtive 
	/**
     * Verifica se tutti gli slot dinamici della stiva sono attualmente occupati.
     * * @return true se la stiva è piena, false altrimenti
     */
    boolean isFull();

    /**
     * Cerca e restituisce il primo slot logico libero disponibile.
     * L'ordine di ricerca è garantito dall'implementazione (es. ordine alfabetico).
     * * @return L'oggetto ISlot libero, oppure null se la stiva è piena
     */
    ISlot findFreeSlot();

    /**
     * Recupera un oggetto Slot logico partendo dal suo nome identificativo.
     * * @param name Il nome dello slot da cercare (es. "slot1")
     * @return L'oggetto ISlot corrispondente, oppure null se non esiste
     */
    ISlot getSlot(String name);

    /**
     * Svuota tutti gli slot correntemente registrati, rimuovendo
     * eventuali container presenti (imposta lo stato a libero).
     */
    void clearAllSlots();
}
