package client;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class UserButtonPressedClient {

    protected String name = "userclient";
    protected boolean connected = false;
    protected Interaction conn;

    protected IApplMessage buttonPressed =
        CommUtils.buildDispatch(name, "buttonPressed", "buttonPressed(user1)", "ioport");

    public void doJob() throws Exception {
        connect();
        test1();
        CommUtils.delay(1000);
        CommUtils.outblue(name + " | ENDS");
        System.exit(0);
    }

    protected void connect() {
        CommUtils.outblue(name + " | connect ");
        if (connected) return;
        connected = true;
        // Context ctxcargosystem: host="localhost" port=8020, protocollo di default TCP
        conn = ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8120");
        CommUtils.outblue(name + " | connect commChannel=" + conn);
    }

    protected void test1() throws Exception {
        conn.forward(buttonPressed);
        CommUtils.outblue(name + " | test1 sent buttonPressed to ioport");
    }

    public static void main(String[] args) {
        try {
            new UserButtonPressedClient().doJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}