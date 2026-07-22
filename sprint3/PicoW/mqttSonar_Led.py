from machine import Pin, time_pulse_us
import network
import time
from secretsEnv import secrets
from umqtt_simple import MQTTClient

# ===== WIFI =====
SSID     = secrets["ssid"]
PASSWORD = secrets["pw"]

led = Pin("LED", Pin.OUT)

def ledOnOff(N, DT):
    for _ in range(N):
        led.toggle()
        time.sleep(DT)
    led.off()

def connect_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(SSID, PASSWORD)
    
    print("Connessione WiFi in corso...")
    while not wlan.isconnected():
        time.sleep(1)
        print(".", end="")
    
    print("\nConnesso:", wlan.ifconfig())
    
def reconnect_mqtt():
    global client
    while True:
        try:
            print("Connessione persa. Tentativo di ripristino in corso...")
            
            # 1. Verifica che il Wi-Fi sia ancora vivo, altrimenti lo riconnette
            wlan = network.WLAN(network.STA_IF)
            if not wlan.isconnected():
                connect_wifi()
            
            # 2. Ritenta la connessione MQTT
            client = connect_mqtt()
            client.subscribe(TOPIC_SUB)
            
            print(f"Riconnesso con successo a MQTT e al topic {TOPIC_SUB.decode()}!")
            return  # Esce dal loop infinito e torna al programma principale
            
        except OSError as e:
            print(f"Riconnessione fallita: {e}. Riprovo tra 5 secondi...")
            time.sleep(5)

# ===== MQTT =====
MQTT_BROKER = secrets["broker_ip"]
MQTT_PORT   = secrets["port"]
CLIENT_ID   = secrets["cliend_ID"]

TOPIC_SUB = b"picosonar/led"  # Topic dove il Pico ascolta i comandi dal QAk
TOPIC_PUB = b"picosonar/data" # Topic dove il Pico pubblica i dati del sonar

# === INIZIALIZZAZIONE VARIABILI PER IL LAMPEGGIAMENTO ===
blinking = False
BLINK_PERIOD_MS = 500  # Tempo in millisecondi tra un toggle e l'altro
last_toggle = time.ticks_ms()
last_sonar = time.ticks_ms()
SONAR_PERIOD_MS = 1000

def on_message(topic, msg):
    global blinking
    s = msg.decode()
    print(f"Ricevuto messaggio su {topic.decode()}: {s}")
    
    # === AGGIORNATO CON I NUOVI NOMI DEI DISPATCH QAK ===
    if "msg(startBlink," in s:
        blinking = True
        print("-> Blinking ATTIVATO")
    elif "msg(stopBlink," in s:
        blinking = False
        led.off()
        print("-> Blinking DISATTIVATO")

def connect_mqtt():
    client = MQTTClient(
        client_id = CLIENT_ID,
        server    = MQTT_BROKER,
        port      = MQTT_PORT,
        keepalive = 60
    )
    client.set_callback(on_message)
    client.connect()
    print(f"Connesso al broker MQTT: {MQTT_BROKER}:{MQTT_PORT}")
    return client

# ===== SONAR =====
TRIG = Pin(3, Pin.OUT)
ECHO = Pin(2, Pin.IN)

def misura_distanza():
    TRIG.low()
    time.sleep_us(2)
    TRIG.high()
    time.sleep_us(10)
    TRIG.low()
    
    durata = time_pulse_us(ECHO, 1, 30000)
    if durata < 0:
        return None
    distanza = (durata / 2) / 29.1
    return distanza

# ===== MAIN =====
try:
    connect_wifi()
    client = connect_mqtt()
    
    client.subscribe(TOPIC_SUB)
    print(f"MQTT iscritto al topic '{TOPIC_SUB.decode()}' e pronto")
    ledOnOff(5, 0.3)

    while True:
        now = time.ticks_ms()
        
        try:
            # 1. attività "sonar"
            if time.ticks_diff(now, last_sonar) >= SONAR_PERIOD_MS:
                last_sonar = now
                d = misura_distanza()
                if d is not None:
                    value = "sonardata(%d)" % round(d)
                    msg_payload = "msg(sonardata,event,picow,none,%s,0)" % value
                    
                    print(f"Publish su {TOPIC_PUB.decode()}: {msg_payload}")
                    client.publish(TOPIC_PUB, msg_payload.encode())

            # 2. attività "led"
            if blinking and time.ticks_diff(now, last_toggle) >= BLINK_PERIOD_MS:
                last_toggle = now
                led.toggle()

            # 3. controlla messaggi
            client.check_msg()

        except OSError as e:
            # Se la rete cade, l'errore viene catturato QUI, senza rompere il "while True"
            print(f"Errore di rete rilevato nel loop: {e}")
            reconnect_mqtt()
            
            # Resetta i timer per evitare che, appena riconnesso, 
            # pubblichi dati istantaneamente con tempi sballati
            last_sonar = time.ticks_ms()
            last_toggle = time.ticks_ms()

        time.sleep_ms(20)  

# Questi except esterni ora catturano solo gli stop manuali o errori gravissimi non di rete
except KeyboardInterrupt:
    print("\nProgramma interrotto manualmente")
except Exception as e:
    print(f"\nÈ successo qualcosa di imprevisto: {type(e).__name__} - {e}")
finally:
    led.off()
    print("Terminato e LED spento.")

