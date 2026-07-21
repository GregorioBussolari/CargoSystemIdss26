%====================================================================================
% testobserverioport description   
%====================================================================================
dispatch( info, changed(SOURCE,TERM) ). %notifica di stato, inviata con updateResource
%====================================================================================
context(ctxcargo, "localhost",  "TCP", "8120").
context(ctxioport, "localhost",  "TCP", "8060").
 qactor( cargoservice, ctxcargo, "external").
  qactor( ioport, ctxioport, "it.unibo.ioport.Ioport").
 static(ioport).
