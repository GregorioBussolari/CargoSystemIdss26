%====================================================================================
% cargosystem description   
%====================================================================================
request( loadRequest, loadRequest(X) ). %Richiesta di carico dall'operatore
reply( loadEngaged, loadEngaged(SLOT) ).  %%for loadRequest
reply( retryLater, retryLater(X) ).  %%for loadRequest
reply( loadRejected, loadRejected(X) ).  %%for loadRequest
%====================================================================================
context(ctxcargosystem, "localhost",  "TCP", "8020").
context(ctxrobotservice26, "localhost",  "TCP", "8025").
context(ctxsensor, "localhost",  "TCP", "8135").
 qactor( cargoservice, ctxcargosystem, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( ioport, ctxcargosystem, "it.unibo.ioport.Ioport").
 static(ioport).
  qactor( sensor, ctxsensor, "it.unibo.sensor.Sensor").
 static(sensor).
