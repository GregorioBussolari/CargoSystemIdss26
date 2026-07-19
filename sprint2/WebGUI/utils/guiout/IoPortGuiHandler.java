package guiout;

import java.awt.Desktop;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Vector;

import org.json.JSONObject;
import org.json.simple.JSONValue;

import io.javalin.*;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import it.unibo.kactor.ActorBasic;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttInteraction;
import unibo.basicomm23.msg.ApplMessage;

/**
 * IoPortGuiHandler
 * ------------------------------------------------------------------
 * Dispositivo di I/O incapsulato nel QActor ioport (pattern integrato).
 * Responsabilità: erogare la pagina HTML/JS statica, gestire la
 * connessione WebSocket verso il browser, tradurre i messaggi JSON
 * (contratto Sprint1) da/verso i messaggi qak dell'attore ioport.
 *
 * Input:  {"type":"buttonPressed"} dal browser
 * Output: {"type":"updateState","payload":{"state":S,"message":M}}
 *
 * Dipendenze: Javalin, org.json.simple, infrastruttura Qak (ActorBasic.autoMsg)
 * ------------------------------------------------------------------
 */
public class IoPortGuiHandler {

    private final String name;
    private final ActorBasic owner;   // TODO VERIFY: tipo corretto, vedi ActorBasicFsm nel Protobook §17
    private WsContext pageCtx;        // singola connessione: una sola pagina attiva

    private String lastState = "disengaged";
    private String lastMessage = "Service working";

    public IoPortGuiHandler(String name, int port, ActorBasic owner) {
        this.name = name;
        this.owner = owner;

        CommUtils.outblue(this.name + " | starting on port " + port);

        var app = Javalin.create(config -> {
            config.jetty.modifyWebSocketServletFactory(factory -> {
                factory.setIdleTimeout(Duration.ofMinutes(30));
            });
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/page";
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(port);

        // --------------------------------------------------
        // HTTP: serve la pagina statica
        // --------------------------------------------------
        app.get("/", ctx -> {
            var inputStream = getClass().getResourceAsStream("/page/index.html");
            if (inputStream != null) {
                String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                ctx.html(content);
            } else {
                ctx.status(404).result("File non trovato");
            }
        });

        // --------------------------------------------------
        // WebSocket: path allineato al frontend (/ws)
        // --------------------------------------------------
        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                pageCtx = ctx;
                CommUtils.outmagenta(name + " | pagina connessa");
                sendUpdate(lastState, lastMessage);
            });

            ws.onClose(ctx -> {
                if (pageCtx == ctx) pageCtx = null;
                CommUtils.outmagenta(name + " | pagina disconnessa");
            });

            ws.onMessage(ctx -> {
                String raw = ctx.message();
                try {
                    // Usa il costruttore di org.json.JSONObject, è più sicuro
                    JSONObject msg = new JSONObject(raw); 
                    
                    // Verifica se il campo "type" esiste e vale "buttonPressed"
                    if (msg.has("type") && "buttonPressed".equals(msg.getString("type"))) {
                        forwardButtonPressed();
                    } else {
                        CommUtils.outred(name + " | messaggio non riconosciuto: " + raw);
                    }
                } catch (Exception e) {
                    CommUtils.outred(name + " | errore parsing JSON: " + e.getMessage() + " | raw: " + raw);
                }
            });
        });
    }

    /**
     * Inietta un dispatch buttonPressed nella coda dell'attore ioport,
     * chiamato dal thread Jetty che gestisce onMessage.
     *
     * TODO VERIFY: firma esatta di autoMsg (nome metodo, tipo parametro,
     * eventuale necessità di passare mittente/destinatario espliciti,
     * eventuale metodo statico su un registro globale invece che
     * sull'istanza dell'attore).
     */
    private void forwardButtonPressed() {
        String rawMsg = "msg(buttonPressed,dispatch,guih," + owner.getName() + ",1,0)";
        // TODO VERIFY: costruzione messaggio - alternativa con CommUtils.buildDispatch:
        IApplMessage m = CommUtils.buildDispatch("guih", "buttonPressed", "1", owner.getName());
        //IApplMessage m = new ApplMessage(rawMsg);
        
        owner.autoMsg(m, null);   // TODO VERIFY: metodo/firma reale
    }

    /**
     * Richiamato dal QActor ioport (via blocco [# gui.updateState(...) #])
     * per notificare un cambio di stato al browser.
     */
    public void updateState(String state, String message) {
        sendUpdate(state, message);
    }

    private void sendUpdate(String state, String message) {
        lastState = state;
        lastMessage = message;

        JSONObject payload = new JSONObject();
        payload.put("state", state);
        payload.put("message", message);

        JSONObject envelope = new JSONObject();
        envelope.put("type", "updateState");
        envelope.put("payload", payload);

        if (pageCtx != null) {
            pageCtx.send(envelope.toString());
        }
    }
}