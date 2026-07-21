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
          ioport=Custom('ioport','./qakicons/symActorWithobjSmall.png')
          led=Custom('led','./qakicons/symActorWithobjSmall.png')
          sensor=Custom('sensor','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxrobotsmart', graph_attr=nodeattr):
          robotsmart=Custom('robotsmart(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='sensorAlarm', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     sys >> Edge( label='containerDetected', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     cargoservice >> Edge( label='timeOut', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     cargoservice >> Edge( label='loadEnded', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='sensorRestored', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     cargorobot >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='sonardata', **evattr, decorate='true', fontcolor='darkgreen') >> sensor
     sensor >> Edge( label='containerDetected', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sensor >> Edge( label='sensorAlarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sensor >> Edge( label='sensorRestored', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     ioport >> Edge(color='magenta', style='solid', decorate='true', label='<loadRequest<font color="darkgreen"> loadEngaged retryLater loadRejected</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     cargorobot >> Edge(color='magenta', style='solid', decorate='true', label='<moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> robotsmart
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<movetoslot<font color="darkgreen"> movetoslotdone movetoslotfailed</font> &nbsp; >',  fontcolor='magenta') >> cargorobot
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<startBlink &nbsp; stopBlink &nbsp; >',  fontcolor='blue') >> led
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<startOOS &nbsp; endOOS &nbsp; >',  fontcolor='blue') >> cargorobot
diag
