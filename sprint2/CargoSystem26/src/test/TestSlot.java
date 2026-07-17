package test;

import static org.junit.Assert.*;

import org.junit.*;

import domain.ISlot;
import domain.Slot;

// ABBIAMO AGGIUNTO 'public' ALLA CLASSE
public class TestSlot {

    private ISlot slot;

    // ABBIAMO AGGIUNTO 'public' AL METODO DI SETUP
    @Before
    public void setUp() {
        // Inizializziamo lo slot prima di ogni test
        System.out.println("Slot setup");
        slot = new Slot("Slot1");
    }

    // ABBIAMO AGGIUNTO 'public' A TUTTI I METODI DI TEST
    @Test
    public void testGetName() {
        // Verifica che il nome inserito nel costruttore sia corretto
        assertEquals("Slot1", slot.getName());
    }

    @Test
    public void testStatoIniziale() {
        // Verifica che uno slot appena creato non sia occupato
        assertFalse(slot.isOccupato());
    }

    @Test
    public void testPutContainer() {
        // Eseguiamo l'inserimento
        slot.putContainer();
        
        // Verifica che lo stato sia cambiato in 'true'
        assertTrue(slot.isOccupato());
    }

    @Test
    public void testRemoveContainer() {
        // Mettiamo prima un container nello slot per riempirlo
        slot.putContainer();
        assertTrue(slot.isOccupato());

        // Rimuoviamo il container
        slot.removeContainer();

        // Verifica che lo stato sia tornato 'false'
        assertFalse(slot.isOccupato());
    }
}