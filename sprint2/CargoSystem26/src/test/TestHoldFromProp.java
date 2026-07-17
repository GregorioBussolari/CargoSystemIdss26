package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.Hold;
import domain.HoldManager;
import domain.IHoldManager;
import domain.ISlot;

public class TestHoldFromProp {

    private IHoldManager hold;
    private final String TEST_FILE_PATH = "properties.txt";

    @Before
    public void tearDown() {
        // Pulizia: eliminiamo il file dopo ogni esecuzione per non sporcare il filesystem
        File testFile = new File(TEST_FILE_PATH);
        
     // 2. Inizializziamo la Hold passando il percorso del file
        // Questo andrà a invocare HoldManifestReader internamente
        hold = new HoldManager(TEST_FILE_PATH);
    }

    @Test
    public void testIsFullQuandoVuota() {
        // Appena caricata dal file, la stiva ha gli slot (1-4) ma sono tutti liberi
        assertFalse(hold.isFull());
    }

    @Test
    public void testFindFreeSlot() {
        hold.clearAllSlots();
        
        // Chiediamo il primo slot libero
        ISlot libero = hold.findFreeSlot();
        
        assertNotNull(libero);
        // L'ordine di lettura dal reader è alfabetico, quindi il primo deve essere "slot1"
        assertEquals("slot1", libero.getName());
    }
    
    @Test
    public void testIsFullQuandoTuttiGliSlotSonoOccupati() {
        assertFalse(hold.isFull());

        // Recuperiamo gli slot caricati dinamicamente dal file e li occupiamo
        hold.getSlot("slot1").putContainer();
        hold.getSlot("slot2").putContainer();
        hold.getSlot("slot3").putContainer();
        hold.getSlot("slot4").putContainer();

        // Ora tutti gli slot rilevanti (1-4) sono occupati -> deve restituire true
        assertTrue(hold.isFull());
    }

    @Test
    public void testFindFreeSlotRestituisceSlot2SeSlot1Occupato() {
        hold.clearAllSlots();
        
        // Occupiamo volontariamente solo il primo slot
        hold.getSlot("slot1").putContainer();

        // Chiediamo il primo slot libero
        ISlot libero = hold.findFreeSlot();

        // Il metodo deve saltare "slot1" e restituire "slot2"
        assertNotNull(libero);
        assertEquals("slot2", libero.getName());
        assertFalse(libero.isOccupato());
    }
    
    @Test
    public void testGetSlotMapEModificaStato() {
        hold.clearAllSlots();
        
        // Recuperiamo un singolo slot creato dal parser
        ISlot slot = hold.getSlot("slot1");
        
        assertNotNull("Lo slot non deve essere null", slot);
        assertFalse("Inizialmente deve essere libero", slot.isOccupato());
        
        // Modifichiamo il suo stato logico
        slot.putContainer();
        
        assertEquals("slot1", slot.getName());
        assertTrue("Ora deve risultare occupato", slot.isOccupato());
    }
    
    @Test
    public void testGetSlotInesistente() {
        // Se cerchiamo una chiave non presente nel file (es. slot5 o inesistente)
        // dato che Hold estrae solo 1-4, deve tornare null
        assertNull(hold.getSlot("slot5"));
        assertNull(hold.getSlot("slotInesistente"));
    }
}