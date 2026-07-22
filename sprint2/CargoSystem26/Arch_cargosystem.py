### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('cargosystemArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxcargosystem', graph_attr=nodeattr):
          cargoservice=Custom('cargoservice','./qakicons/symActorWithobjSmall.png')
          cargorobot=Custom('cargorobot','./qakicons/symActorWithobjSmall.png')
          led=Custom('led','./qakicons/symActorWithobjSmall.png')
          sensor=Custom('sensor','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxrobotsmart', graph_attr=nodeattr):
          robotsmart=Custom('robotsmart(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxioport', graph_attr=nodeattr):
          ioport=Custom('ioport(ext)','./qakicons/externalQActor.png')
     cargoservice >> Edge( label='startSensorRecording', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='containerDetected', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     sys >> Edge( label='sensorAlarm', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     cargoservice >> Edge( label='timeOut', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     cargoservice >> Edge( label='loadEnded', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='sensorRestored', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     cargorobot >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     cargorobot >> Edge( label='loadEnded', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='startSensorRecording', **evattr, decorate='true', fontcolor='darkgreen') >> sensor
     sensor >> Edge( label='containerDetected', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='timeOut', **evattr, decorate='true', fontcolor='darkgreen') >> sensor
     sys >> Edge( label='loadEnded', **evattr, decorate='true', fontcolor='darkgreen') >> sensor
     cargorobot >> Edge(color='magenta', style='solid', decorate='true', label='<moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> robotsmart
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<movetoslot<font color="darkgreen"> movetoslotdone movetoslotfailed</font> &nbsp; >',  fontcolor='magenta') >> cargorobot
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<startBlink &nbsp; stopBlink &nbsp; >',  fontcolor='blue') >> led
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<endOOS &nbsp; startOOS &nbsp; >',  fontcolor='blue') >> cargorobot
diag
