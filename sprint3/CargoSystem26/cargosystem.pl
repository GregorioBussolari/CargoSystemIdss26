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
event( alarm, alarm(X) ). %event at application level
request( loadRequest, loadRequest(X) ). %Richiesta di carico dall'operatore
reply( loadEngaged, loadEngaged(SLOT) ).  %%for loadRequest
reply( retryLater, retryLater(X) ).  %%for loadRequest
reply( loadRejected, loadRejected(X) ).  %%for loadRequest
dispatch( buttonPressed, buttonPressed(X) ). %Richiesta di carico da parte di un utente
event( timeOut, timeOut(X) ). %Scadenza regola dei 30 secondi
event( loadEnded, loadEnded(X) ). %Fine procedura di carico
dispatch( startOOS, startOOS(X) ). %Inizio stato Out Of Service
dispatch( endOOS, endOOS(X) ). %Fine stato Out Of Service
event( containerDetected, containerDetected(X) ). %Emesso se D < DFREE/2 per 3s
event( sensorAlarm, sensorAlarm(X) ). %Emesso se D > DFREE per 3s
event( sensorRestored, sensorRestored(X) ). %Emesso se D < DFREE per 3s
event( sonardata, sonardata(D) ). %distanza grezza pubblicata dal PicoW
dispatch( startBlink, startBlink(X) ). %Gestione accensione blink LED
dispatch( stopBlink, stopBlink(X) ). %Gestione spegnimento blink LED
event( startSensorRecording, startSensortRecording(X) ). %Inizio fase di rilevazione dei 30 secondi
request( movetoslot, movetoslot(SLOTN) ). %Richiesta di spostamento, Data la stringa identificativa dello slot
reply( movetoslotdone, movetoslottdone(ARG) ).  %%for movetoslot
reply( movetoslotfailed, movetoslotfailed(PLANDONE,PLANTODO) ).  %%for movetoslot
%====================================================================================
context(ctxcargosystem, "localhost",  "TCP", "8120").
context(ctxrobotsmart, "robotsmart26",  "TCP", "8020").
context(ctxioport, "webgui",  "TCP", "8040").
 qactor( robotsmart, ctxrobotsmart, "external").
  qactor( ioport, ctxioport, "external").
  qactor( cargoservice, ctxcargosystem, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargorobot, ctxcargosystem, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( led, ctxcargosystem, "it.unibo.led.Led").
 static(led).
  qactor( sensor, ctxcargosystem, "it.unibo.sensor.Sensor").
 static(sensor).
