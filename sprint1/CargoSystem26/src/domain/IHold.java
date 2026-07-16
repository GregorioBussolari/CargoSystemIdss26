package domain;

public interface IHold {
	/**
     * Restituisce le coordinate spaziali associate a un dato slot logico o punto di interesse.
     * * @param slotName Il nome identificativo dello slot (es. "ioport", "slot1", "slot5")
     * @return L'oggetto {@link Hold.Coord} contenente le coordinate x e y
     * @throws IllegalArgumentException se lo slot richiesto non esiste o non è configurato
     */
    Hold.Coord coordinatesOf(String slotName);

    /**
     * Restituisce la larghezza della stiva espressa in unità robotiche.
     * * @return larghezza della hold
     */
    int getWidth();

    /**
     * Restituisce la lunghezza della stiva espressa in unità robotiche.
     * * @return lunghezza della hold
     */
    int getLength();

    /**
     * Restituisce la dimensione di un'unità robotica espressa in metri (D).
     * * @return dimensione D in metri
     */
    int getD();
}
