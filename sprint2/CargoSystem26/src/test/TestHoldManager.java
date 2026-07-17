package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.Hold;
import domain.HoldManager;
import domain.IHoldManager;
import domain.ISlot;

public class TestHoldManager {

    private IHoldManager hold;

    @Before
    public void setUp() {
        // 1. Definiamo solo i nomi degli slot logici (senza coordinate!)
        List<String> slotNames = Arrays.asList("Slot1", "Slot2", "Slot3", "Slot4");
        
        // 2. Inizializziamo la Hold usando il costruttore per i test
        // (Passiamo dimensioni fittizie 8x6 e D=1, più la lista dei nomi)
        hold = new HoldManager(slotNames); 
    }

    @Test
    public void testIsFullQuandoVuota() {
        // Appena creata, la stiva ha tutti gli slot liberi
        assertFalse(hold.isFull());
    }

    @Test
    public void testFindFreeSlot() {
        // Assicuriamoci che sia tutto pulito
        hold.clearAllSlots();
        
        // Chiediamo il primo slot libero
        ISlot libero = hold.findFreeSlot();
        
        assertNotNull(libero);
        // Poiché li abbiamo inseriti in ordine, il primo deve essere "Slot1"
        assertEquals("Slot1", libero.getName());
    }
    
    @Test
    public void testIsFullQuandoTuttiGliSlotSonoOccupati() {
        assertFalse(hold.isFull());

        // Recuperiamo gli slot creati internamente dalla Hold e li occupiamo
        hold.getSlot("Slot1").putContainer();
        hold.getSlot("Slot2").putContainer();
        hold.getSlot("Slot3").putContainer();
        hold.getSlot("Slot4").putContainer();

        // Ora tutti gli slot (1-4) sono occupati -> deve restituire true
        assertTrue(hold.isFull());
    }

    @Test
    public void testFindFreeSlotRestituisceSlot2SeSlot1Occupato() {
        hold.clearAllSlots();
        
        // Occupiamo volontariamente solo lo Slot1
        hold.getSlot("Slot1").putContainer();

        // Chiediamo il primo slot libero
        ISlot libero = hold.findFreeSlot();

        // Il metodo deve saltare lo Slot1 e restituire lo Slot2
        assertNotNull(libero);
        assertEquals("Slot2", libero.getName());
        assertFalse(libero.isOccupato());
    }
    
    @Test
    public void testGetSlotMapEModificaStato() {
        hold.clearAllSlots();
        
        // Recuperiamo un singolo slot
        ISlot slot = hold.getSlot("Slot1");
        
        assertNotNull("Lo slot non deve essere null", slot);
        assertFalse("Inizialmente deve essere libero", slot.isOccupato());
        
        // Modifichiamo il suo stato
        slot.putContainer();
        
        assertEquals("Slot1", slot.getName());
        assertTrue("Ora deve risultare occupato", slot.isOccupato());
    }
    
    @Test
    public void testGetSlotInesistente() {
        // Se cerchiamo uno slot che non abbiamo passato nella lista iniziale, deve tornare null
        assertNull(hold.getSlot("SlotInesistente"));
    }
}