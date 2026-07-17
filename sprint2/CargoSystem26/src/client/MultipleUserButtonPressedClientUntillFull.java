package client;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class MultipleUserButtonPressedClientUntillFull {

    protected String name = "userclient";
    protected boolean connected = false;
    protected Interaction conn;

    // Primo messaggio
    protected IApplMessage buttonPressed1 =
        CommUtils.buildDispatch(name, "buttonPressed", "buttonPressed(user1)", "ioport");
        
    // Secondo messaggio 
    protected IApplMessage buttonPressed2 =
        CommUtils.buildDispatch(name, "buttonPressed", "buttonPressed(user2)", "ioport");

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
       for(int i = 0; i<5; i++){ //invio 5 request corrette
    	   // 1. Premo primo pulsante
           conn.forward(buttonPressed1);
           CommUtils.outblue(name + " | test1 sent "+i+1+" buttonPressed to ioport");
           
           // 2. Attesa
           int delayTime = 45000;
           CommUtils.outblue(name + " | waiting for " + delayTime + " ms before "+i+1+" press...");
           CommUtils.delay(delayTime);   
       }
        
    }

    public static void main(String[] args) {
        try {
            new MultipleUserButtonPressedClientUntillFull().doJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}