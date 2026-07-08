package test;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import domain.Hold;
import domain.Hold.Coord;
import domain.Slot; // Assumiamo che Slot implementi ISlot
import domain.ISlot;

public class TestHold {

    private Hold hold;
    private Coord c1, c2, c3, c4, c5, cIoPort;
    private Slot slot1, slot2, slot3, slot4;

    @Before
    public void setUp() {
        // Inizializziamo la Hold reale
        hold = new Hold(10, 10,1);

        // Definiamo le coordinate reali usando Hold.Coord
        c1 = new Coord(0, 0);
        c2 = new Coord(0, 1);
        c3 = new Coord(1, 0);
        c4 = new Coord(1, 1);
        c5 = new Coord(2, 2); // Lo slot 5 speciale
        cIoPort = new Coord(5, 5);

        // Impostiamo le coordinate speciali nella hold
        hold.setSlot5(c5);
        hold.setIoport(cIoPort);

        // Creiamo gli oggetti Slot reali (assumendo che il costruttore prenda il nome)
        slot1 = new Slot("Slot1");
        slot2 = new Slot("Slot2");
        slot3 = new Slot("Slot3");
        slot4 = new Slot("Slot4");
        
        
        hold.addSlot(c1, slot1);
        hold.addSlot(c2, slot2);
        hold.addSlot(c3, slot3);
        hold.addSlot(c4, slot4);
        
        
        // NOTA: Per fare il test dobbiamo simulare il riempimento di `gridSlots`.
        // Se la tua classe Hold non ha un metodo public per aggiungere slot, 
        // puoi usare la reflection o aggiungere un metodo "addSlot" in Hold per il setup.
        // Qui simuliamo il comportamento assumendo di aver popolato la mappa.
    }

    @Test
    public void testGetIoPortEGetSlot5() {
        // Verifica che i getter delle coordinate speciali funzionino
        assertEquals(cIoPort, hold.getIoPort());
        assertEquals(c5, hold.getSlot5());
    }

    @Test
    public void testIsFullQuandoVuota() {
        // Se la mappa è vuota o slot5 è null, deve ritornare false
        assertFalse(hold.isFull());
    }

    @Test
    public void testGetSlotCoord() {
        // Questo test verifica il funzionamento del metodo di ricerca inversa getSlotCoord
        // (Funziona se gli slot sono inseriti nella mappa gridSlots della hold)
        
        // Verifica del caso di slot inesistente
        assertNull(hold.getSlotCoord(new Slot("Inesistente")));
    }

    @Test
    public void testFindFreeSlot() {
        // Se la mappa è vuota, deve restituire null
    	hold.clearAllSlots();
    	ISlot libero = hold.findFreeSlot();
    	assertNotNull(libero);
        assertEquals("Slot1", libero.getName());
    }
    
    @Test
    public void testIsFullQuandoTuttiGliSlotSonoOccupati() {
        // All'inizio sono tutti liberi, quindi isFull() deve essere false
        assertFalse(hold.isFull());

        // Occupiamo gli slot da 1 a 4
        slot1.putContainer();
        slot2.putContainer();
        slot3.putContainer();
        slot4.putContainer();

        // Lo slot5 lo lasciamo libero volutamente per verificare che venga ignorato
        // secondo la logica del tuo metodo filter(e -> !e.getKey().equals(slot5))
        
        // Ora tutti gli slot rilevanti (1-4) sono occupati -> deve restituire true
        assertTrue(hold.isFull());
    }

    @Test
    public void testFindFreeSlotRestituisceSlot2SeSlot1EAltroveOccupato() {
        // Situazione iniziale: tutti liberi, findFreeSlot prenderà il primo disponibile dal flusso
    	hold.clearAllSlots();
        // Per testare in modo deterministico, occupiamo lo slot1
        slot1.putContainer();

        // Ora slot1 è occupato. Chiediamo il primo slot libero
        ISlot libero = hold.findFreeSlot();

        // Verifichiamo che il metodo non restituisca null e che lo slot trovato sia proprio lo slot2
        assertNotNull(libero);
        assertEquals("Slot2", libero.getName());
        assertFalse(libero.isOccupato());
    }
    
    @Test
    public void testGetSlotMap() {
        // Situazione iniziale: tutti liberi, findFreeSlot prenderà il primo disponibile dal flusso
    	hold.clearAllSlots();
        
        ISlot slot = hold.getSlot("Slot1");
        assertFalse(slot.isOccupato());
        
        slot.putContainer();
        assertEquals("Slot1", slot.getName());
        assertTrue(slot.isOccupato());
        
        
    }
    

}
