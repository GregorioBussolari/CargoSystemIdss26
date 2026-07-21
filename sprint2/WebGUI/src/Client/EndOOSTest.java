package Client;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

/**
 * EndOOSTest
 * ------------------------------------------------------------------
 * Simula il ciclo sensorAlarm -> sensorRestored per verificare che
 * CargoService entri in outOfService e ne esca correttamente, e che
 * IOPort rifletta entrambe le transizioni tramite il canale CoAP.
 * ------------------------------------------------------------------
 */
public class EndOOSTest {

	public static void main(String[] args) throws Exception {
        Interaction conn = ConnectionFactory.createClientSupport(
            ProtocolType.tcp, "localhost", "8120");
        conn.forward(CommUtils.buildEvent("testoos", "sensorRestored", "1"));
        Thread.sleep(2000);
        conn.close();
    }
}