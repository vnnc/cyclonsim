import networkx as nx
import pylab as pyb

def import_graph(path) -> nx.DiGraph:
    csv_file = open(path,'rb')
    G = nx.read_adjlist(csv_file,delimiter=",")
    return G

def render_graph(graph: nx.DiGraph,label):
    pyb.figure(label)
    nx.draw_networkx(graph,pos=nx.kamada_kawai_layout(graph),with_labels=True)

files: str
files = input("Fichiers à ouvrir (separés par un espace): ")
filelist = files.split(" ")
for i in range(0,len(filelist)):
    G = import_graph(filelist[i])
    render_graph(G,i)
pyb.show()
