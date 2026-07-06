%====================================================================================
% cargosystem description   
%====================================================================================
event( alarm, alarm(X) ). %Allarme emesso dal robot
dispatch( move, move(M) ). %MOVE = l|r|a|d|h  mosse ARIL sincrone
dispatch( setrobotstate, setpos(X,Y,D) ). %Forza posizione (X,Y) e direzione D
request( setdirection, dir(D) ). %Richiesta rotazione D=up|down|left|right
reply( setdirectiondone, pos(PX,PY) ).  %%for setdirection
request( getrobotstate, getrobotstate(ARG) ). %Richiesta stato corrente
reply( robotstate, robotstate(POS,DIR) ).  %%for getrobotstate
request( loadRequest, loadRequest(X) ). %Richiesta di carico dall'operatore
reply( loadEngaged, loadEngaged(SLOT) ).  %%for loadRequest
reply( retryLater, retryLater(X) ).  %%for loadRequest
reply( loadRejected, loadRejected(X) ).  %%for loadRequest
dispatch( buttonPressed, buttonPressed(X) ). %Richiesta di carico da parte di un utente
event( timeOut, timeOut(X) ). %Scadenza regola dei 30 secondi
event( loadEnded, loadEnded(X) ). %Fine procedura di carico
event( endOOS, endOOS(X) ). %Fine stato Out Of Service
event( containerDetected, containerDetected(X) ). %Emesso se D < DFREE/2 per 3s
event( sensorAlarm, sensorAlarm(X) ). %Emesso se D > DFREE per 3s
dispatch( startBlink, startBlink(X) ). %Gestione accensione blink LED
dispatch( stopBlink, stopBlink(X) ). %Gestione spegnimento blink LED
request( doplan, doplan(PLAN,STEPTIME) ). %execute PLAN with STEPTIME
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(PLANTODO) ).  %%for doplan
dispatch( nextmove, nextmove(M) ). %autodispatch
dispatch( nomoremove, nomoremove(M) ). %autodispatch
event( alarm, alarm(X) ). %event at application level
%====================================================================================
context(ctxcargosystem, "localhost",  "TCP", "8020").
 qactor( cargoservice, ctxcargosystem, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( ioport, ctxcargosystem, "it.unibo.ioport.Ioport").
 static(ioport).
  qactor( led, ctxcargosystem, "it.unibo.led.Led").
 static(led).
  qactor( sensor, ctxcargosystem, "it.unibo.sensor.Sensor").
 static(sensor).
