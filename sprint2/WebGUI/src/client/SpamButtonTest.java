package client;

import org.eclipse.californium.core.CoapClient;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;

import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.CommUtils;

public class SpamButtonTest {

    public static void main(String[] args) {
        try {
            CommUtils.outmagenta("=== [SpamUserClickClient] Inizio Test (Verifica Pattern Sink) ===");

            CoapClient coapCargo = new CoapClient("coap://127.0.0.1:8120/ctxcargosystem/cargoservice");
            CoapClient coapIoPort = new CoapClient("coap://127.0.0.1:8040/ctxioport/ioport");

            CommUtils.outcyan("Stato INIZIALE CargoService: " + coapCargo.get().getResponseText());
            CommUtils.outcyan("Stato INIZIALE IOPort: " + coapIoPort.get().getResponseText());

            Interaction tcpIoPort = TcpClientSupport.connect("127.0.0.1", 8040, 10);
            
            IApplMessage msg = CommUtils.buildDispatch("spamClient", "buttonPressed", "buttonPressed(click)", "ioport");

            // Simulazione dello SPAM: 5 click in rapida successione
            CommUtils.outmagenta(">>> Inizio invio raffica di 5 messaggi ravvicinati...");
            for(int i = 1; i <= 5; i++) {
                CommUtils.outgreen("  -> Inviando click numero " + i + "...");
                tcpIoPort.forward(msg.toString());
                Thread.sleep(100); // 100ms tra un click e l'altro
            }

            CommUtils.outmagenta("Attesa 3 secondi per elaborazione FSM e pattern sink...");
            Thread.sleep(3000);

            CommUtils.outcyan("Stato FINALE CargoService: " + coapCargo.get().getResponseText());
            CommUtils.outcyan("Stato FINALE IOPort: " + coapIoPort.get().getResponseText());

            tcpIoPort.close();
            CommUtils.outmagenta("=== [SpamUserClickClient] Fine Test ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}