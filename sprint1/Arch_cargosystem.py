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
          ioport=Custom('ioport','./qakicons/symActorWithobjSmall.png')
          led=Custom('led','./qakicons/symActorWithobjSmall.png')
          sensor=Custom('sensor','./qakicons/symActorWithobjSmall.png')
     sys >> Edge( label='containerDetected', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     cargoservice >> Edge( label='timeOut', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     cargoservice >> Edge( label='loadEnded', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='endOOS', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     ioport >> Edge(color='magenta', style='solid', decorate='true', label='<loadRequest<font color="darkgreen"> loadEngaged retryLater loadRejected</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<startBlink &nbsp; stopBlink &nbsp; >',  fontcolor='blue') >> led
diag
