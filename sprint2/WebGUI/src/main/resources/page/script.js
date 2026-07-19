// Stabilisce la connessione WebSocket usando l'host corrente
const wsProtocol = window.location.protocol === "https:" ? "wss://" : "ws://";
const socket = new WebSocket(wsProtocol + window.location.host + "/ws");

// Riferimenti agli elementi del DOM
const displayArea = document.getElementById("displayArea");
const displayMessage = document.getElementById("displayMessage");
const connectionStatus = document.getElementById("connectionStatus");
const statusIndicator = document.querySelector(".status-indicator");

// Gestione apertura connessione
socket.onopen = function () {
    connectionStatus.innerText = "ONLINE";
    statusIndicator.classList.add("connected");
    updateUI("disengaged", "Sistema operativo. In attesa di input.");
};

// Gestione chiusura connessione
socket.onclose = function () {
    connectionStatus.innerText = "OFFLINE";
    statusIndicator.classList.remove("connected");
    updateUI("outofservice", "Connessione con il server persa.");
};

// Gestione messaggi in ingresso (Backend -> Frontend)
socket.onmessage = function (event) {
    try {
        const data = JSON.parse(event.data);

        // Verifica se il messaggio è di tipo "updateState" come da analisi
        if (data.type === "updateState" && data.payload) {
            updateUI(data.payload.state, data.payload.message);
        }
    } catch (e) {
        console.error("Errore nel parsing del messaggio JSON: ", e);
    }
};

// Aggiorna l'interfaccia visiva
function updateUI(stateClass, messageText) {
    displayMessage.innerText = messageText;
    // Resetta le classi di stato e applica quella nuova
    displayArea.className = "display-card " + stateClass;
}

// Funzione richiamata dalla pressione del pulsante "REQUEST LOAD" (Frontend -> Backend)
function sendLoadRequest() {
    if (socket.readyState === WebSocket.OPEN) {
        const msg = { type: "buttonPressed" };
        socket.send(JSON.stringify(msg));
    } else {
        alert("Errore: la connessione con il server non è attiva.");
    }
}