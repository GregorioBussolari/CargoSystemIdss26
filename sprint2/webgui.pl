%====================================================================================
% webgui description   
%====================================================================================
dispatch( buttonPressed, buttonPressed(X) ). %Pressione bottone da GUI
request( loadRequest, loadRequest(X) ). %Richiesta di carico verso CargoService
reply( loadEngaged, loadEngaged(SLOT) ).  %%for loadRequest
reply( retryLater, retryLater(X) ).  %%for loadRequest
reply( loadRejected, loadRejected(X) ).  %%for loadRequest
%====================================================================================
context(ctxioport, "localhost",  "TCP", "8040").
context(ctxcargosystem, "localhost",  "TCP", "8120").
 qactor( cargoservice, ctxcargosystem, "external").
  qactor( ioport, ctxioport, "it.unibo.ioport.Ioport").
 static(ioport).
