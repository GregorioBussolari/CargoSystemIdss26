package guiout;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.unibo.kactor.ActorBasic;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.interfaces.IApplMessage;

public class CargoServiceCoapObserver {

    private final CoapClient client;
    private final IoPortGuiHandler guiHandler;
    private final ActorBasic owner;
    private final String uri;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public CargoServiceCoapObserver(String host, int port, String ctx, String actor,
                                     IoPortGuiHandler guiHandler, ActorBasic owner) {
        this.guiHandler = guiHandler;
        this.owner = owner;

        this.uri = "coap://" + host + ":" + port + "/" + ctx + "/" + actor;
        CommUtils.outcyan("CargoServiceCoapObserver | Attivazione su: " + uri);

        this.client = new CoapClient(uri);
        startObserve();
    }

    private void startObserve() {
        this.client.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                try {
                    // Parsing del JSON ricevuto da cargoservice
                    JSONObject json = new JSONObject(content);
                    String state = json.getString("state");
                    String message = json.getString("message");
                    String holdInfo = json.getString("hold");
                    
                    // Aggiornamento GUI
                    if(guiHandler != null) {
                        guiHandler.updateState(state, message + " | Hold: " + holdInfo);
                    }
                    
                    // Update stato IOPort
                    String payloadQak = "cargoStateChanged(" + state + ")";
                    
                    IApplMessage msg = CommUtils.buildDispatch("observer", "cargoStateChanged", payloadQak, owner.getName());
                    owner.autoMsg(msg, null);
                    
                } catch (Exception e) {
                    CommUtils.outred("CargoServiceCoapObserver | Errore nel parsing del payload: " + content + " | Dettaglio: " + e.getMessage());
                }
            }

            @Override
            public void onError() {
                CommUtils.outred("CargoServiceCoapObserver | Connessione CoAP fallita/interrotta verso " + uri + ". Tentativo di riconnessione tra 5s...");
                scheduler.schedule(() -> startObserve(), 5, TimeUnit.SECONDS);
            }
        });
    }
}