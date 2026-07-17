package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.Hold;
import domain.Hold.Coord;
import domain.IHold;


public class TestHold {

    private IHold hold;
    private final String TEST_FILE_PATH = "properties.txt";

    @Before
    public void setUp() throws Exception {
        // 1. Creazione del file manifest temporaneo per il test
        File testFile = new File(TEST_FILE_PATH);

        // 2. Inizializzazione della classe (che invocherà HoldManifestReader internamente)
        hold = new Hold(TEST_FILE_PATH);
    }


    @Test
    public void testCoordinatesOfAreeSpeciali() {
        // Verifica che la IOPort sia stata mappata correttamente
        Coord ioPort = hold.coordinatesOf("ioport");
        assertEquals(new Coord(4, 0), ioPort);

        // Verifica che lo slot5 sia stato mappato correttamente
        Coord slot5 = hold.coordinatesOf("slot5");
        assertEquals(new Coord(2, 5), slot5);
    }

    @Test
    public void testCoordinatesOfSlotStandard() {
        // Verifica degli slot dinamici
        assertEquals(new Coord(1, 1), hold.coordinatesOf("slot1"));
        assertEquals(new Coord(1, 4), hold.coordinatesOf("slot2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinatesOfSlotInesistenteLanciaEccezione() {
        // Se chiediamo una coordinata che non esiste nel manifest, 
        // la classe DEVE lanciare IllegalArgumentException come da codice
        hold.coordinatesOf("slotInesistente");
    }
    
    @Test
    public void testLetturaDimensioniDaFile() {
        // Verifica che le dimensioni fisiche siano state lette dal parser 
        // e salvate correttamente nello stato della Hold
        assertEquals(6, hold.getWidth());
        assertEquals(5, hold.getLength());
        assertEquals(1, hold.getD());
    }
    
}