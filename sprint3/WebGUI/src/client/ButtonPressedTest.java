package client;

import unibo.basicomm23.utils.CommUtils;

import org.eclipse.californium.core.CoapClient;

import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.tcp.TcpClientSupport;

public class ButtonPressedTest {
	public static void main(String[] args) {
        try {
            CommUtils.outmagenta("=== [SingleUserClickClient] Inizio Test (Singola Interazione) ===");

            
            CoapClient coapCargo = new CoapClient("coap://127.0.0.1:8120/ctxcargosystem/cargoservice");
            CoapClient coapIoPort = new CoapClient("coap://127.0.0.1:8040/ctxioport/ioport");

            CommUtils.outcyan("Stato INIZIALE CargoService: " + coapCargo.get().getResponseText());
            CommUtils.outcyan("Stato INIZIALE IOPort: " + coapIoPort.get().getResponseText());

            Interaction tcpIoPort = TcpClientSupport.connect("127.0.0.1", 8040, 10);
            
            String msg = "msg(buttonPressed, dispatch, singleClient, ioport, buttonPressed(click), 1)";

            CommUtils.outgreen(">>> Invio richiesta di carico (buttonPressed)...");
            tcpIoPort.forward(msg);

            CommUtils.outmagenta("Attesa 2 secondi ");
            Thread.sleep(2000);

            CommUtils.outcyan("Stato FINALE CargoService: " + coapCargo.get().getResponseText());
            CommUtils.outcyan("Stato FINALE IOPort: " + coapIoPort.get().getResponseText());

            tcpIoPort.close();
            CommUtils.outmagenta("=== [SingleUserClickClient] Fine Test ===");

        } catch (Exception e) {
            CommUtils.outred("ERRORE di connessione: Assicurati di aver avviato MainCtxcargosystem (8120) e MainCtxioport (8040)");
            e.printStackTrace();
        }
    }
}
