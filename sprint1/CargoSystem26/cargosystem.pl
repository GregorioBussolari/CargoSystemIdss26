%====================================================================================
% cargosystem description   
%====================================================================================
request( buildPlan, buildPlan(PX,PY,TX,TY) ). %create plan from (PX,PY) to (TX,TY)
reply( buildPlanDone, buildPlanDone(PLAN) ).  %%for buildPlan
request( moverobot, moverobot(TARGETX,TARGETY,STEPTIME) ). %move from curr pos to (TARGETX,TARGETY)
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
dispatch( noplan, noplan(X) ). %autodispatch
dispatch( setplanbuildelay, value(V) ). %parameter = V >= 0
request( loadRequest, loadRequest(X) ). %Richiesta di carico dall'operatore
reply( loadEngaged, loadEngaged(SLOT) ).  %%for loadRequest
reply( retryLater, retryLater(X) ).  %%for loadRequest
reply( loadRejected, loadRejected(X) ).  %%for loadRequest
dispatch( buttonPressed, buttonPressed(X) ). %Richiesta di carico da parte di un utente
event( timeOut, timeOut(X) ). %Scadenza regola dei 30 secondi
event( loadEnded, loadEnded(X) ). %Fine procedura di carico
dispatch( updateHoldState, updateHoldState(FREESLOT) ). %Fine procedura di carico, comunica anche il numero di slot rimanenti
event( endOOS, endOOS(X) ). %Fine stato Out Of Service
event( containerDetected, containerDetected(X) ). %Emesso se D < DFREE/2 per 3s
event( sensorAlarm, sensorAlarm(X) ). %Emesso se D > DFREE per 3s
dispatch( startBlink, startBlink(X) ). %Gestione accensione blink LED
dispatch( stopBlink, stopBlink(X) ). %Gestione spegnimento blink LED
event( startSensorRecording, startSensortRecording(X) ). %Inizio fase di rilevazione dei 30 secondi
%====================================================================================
context(ctxcargosystem, "localhost",  "TCP", "8120").
context(ctxrobotsmart, "127.0.0.1",  "TCP", "8020").
 qactor( robotsmart, ctxrobotsmart, "external").
  qactor( cargoservice, ctxcargosystem, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( ioport, ctxcargosystem, "it.unibo.ioport.Ioport").
 static(ioport).
  qactor( led, ctxcargosystem, "it.unibo.led.Led").
 static(led).
  qactor( sensor, ctxcargosystem, "it.unibo.sensor.Sensor").
 static(sensor).
