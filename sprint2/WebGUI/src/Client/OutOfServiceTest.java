package Client;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;



public class OutOfServiceTest {
	public static void main(String[] args) throws Exception {
        Interaction conn = ConnectionFactory.createClientSupport(
            ProtocolType.tcp, "localhost", "8120");
        conn.forward(CommUtils.buildEvent("testclient", "sensorAlarm", "1"));
        // verifica manuale: il log di cargoservice deve mostrare
        // "[OUT OF SERVICE]", e il CoAP observer di ioport deve
        // ricevere {"state":"outofservice",...}
        Thread.sleep(2000);
        conn.close();
    }
}
