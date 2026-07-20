package guiout;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONObject;
import unibo.basicomm23.utils.CommUtils;

public class CargoServiceCoapObserver {

    private final CoapClient client;
    private final IoPortGuiHandler guiHandler;

    public CargoServiceCoapObserver(String host, int port, String ctx, String actor, IoPortGuiHandler guiHandler) {
        this.guiHandler = guiHandler;
        
        String uri = "coap://" + host + ":" + port + "/" + ctx + "/" + actor;
        CommUtils.outcyan("CargoServiceCoapObserver | Attivazione su: " + uri);
        
        this.client = new CoapClient(uri);
        this.client.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                CommUtils.outcyan("CargoServiceCoapObserver | Ricevuto: " + content);
                
                try {
                    // Parsing nativo del JSON inviato dal QActor
                    JSONObject json = new JSONObject(content);
                    
                    if (json.has("state") && json.has("message")) {
                        String state = json.getString("state");
                        String message = json.getString("message");
                        String holdInfo = json.optString("hold", "N/A");
                        
                        // Inoltro diretto alla WebSocket
                        guiHandler.updateState(state, message + " | Hold: " + holdInfo);
                    }
                } catch (Exception e) {
                    CommUtils.outred("CargoServiceCoapObserver | Il payload non è un JSON valido: " + content);
                }
            }

            @Override
            public void onError() {
                CommUtils.outred("CargoServiceCoapObserver | Connessione CoAP fallita verso " + uri);
            }
        });
    }
}