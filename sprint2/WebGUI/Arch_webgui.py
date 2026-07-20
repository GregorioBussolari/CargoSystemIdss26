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
with Diagram('webguiArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxioport', graph_attr=nodeattr):
          ioport=Custom('ioport','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxcargosystem', graph_attr=nodeattr):
          cargoservice=Custom('cargoservice(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='loadEnded', **evattr, decorate='true', fontcolor='darkgreen') >> ioport
     sys >> Edge( label='timeOut', **evattr, decorate='true', fontcolor='darkgreen') >> ioport
     ioport >> Edge(color='magenta', style='solid', decorate='true', label='<loadRequest<font color="darkgreen"> loadEngaged retryLater loadRejected</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
diag
