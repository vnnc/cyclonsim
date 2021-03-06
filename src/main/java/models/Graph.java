package models;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.io.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Graph<T1,T2> extends DefaultDirectedGraph<T1, T2> {

	Class vertexClass;
	Class edgeClass;

	public Graph(final Class vertexClass, final Class edgeClass) {
		super(new EdgeFactory<T1, T2>() {
			@Override
			public T2 createEdge(T1 t1, T1 v1) {
				T2 edge = null;
				try {
					edge = (T2) edgeClass.getDeclaredConstructor(Object.class,Object.class).newInstance(t1,v1);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				return edge;
			}
		});
		this.edgeClass = edgeClass;
		this.vertexClass = vertexClass;
	}

	/*
	 * Génère un graphe aléatoire, où "vertexAmount" est le nombre de nœuds, et
	 * "edgeProba" est la probabilité qu'une arète potentielle soit construite.
	 * Si edgeProba vaut 0.0 le graphe n'aura aucune arète, si edgeProba vaut 1.0
	 * le graphe sera complet.
	 * FIXME s'assure-t-on que le graphe soit connexe ?
	 */
	public void generateRandom(int vertexAmount, int partialViewSize) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

		Random rng = new Random();

		// Ajout de noeuds *vertexAmount
		for(int i=0;i<vertexAmount;i++){
			T1 vertexToAdd = (T1) vertexClass.getDeclaredConstructor(int.class).newInstance(i);
			this.addVertex(vertexToAdd);
		}

		Set<T1> vertexSet = this.vertexSet();

		for(T1 x : vertexSet){
			AbstractNode node = (AbstractNode) x;
			int nbEdges = 0;
			while(nbEdges<partialViewSize){
				T1 y = this.getNodeByLabel(rng.nextInt(vertexSet.size()));
				AbstractNode edgeTarget = (AbstractNode) y;
				if(edgeTarget.getLabel()!=node.getLabel() && this.getEdge(x,y)==null){
					this.addEdge(x,y);
					nbEdges++;
				}
			}
		}
	}

	public T1 getNodeByLabel(int label) {
		Set<T1> nodes = this.vertexSet();
		Iterator<T1> niter = nodes.iterator();
		while(niter.hasNext()) {
			AbstractNode next = (AbstractNode) niter.next();
			if(next.getLabel() == label) {
				return (T1) next;
			}
		}
		return (T1) new EmptyNode();
	}

	/*
	 * Retourne la liste des nœuds voisins du nœud "node" dans le graphe, c'est-
	 * -à-dire sa "partial view".
	 */
	public ArrayList<T1> getNeighborsOfNode(T1 node) {
		ArrayList<T1> res = new ArrayList<T1>();
		Set<T2> edges_set = this.edgesOf(node);
		for(T2 edge : edges_set) {
			AbstractNode target = (AbstractNode) this.getEdgeTarget(edge);
			if(target.getLabel()!=((AbstractNode) node).getLabel()) {
				res.add(this.getEdgeTarget(edge));
			}
		}
		return res;
	}

	/*
	 * Construit le graphe à partir d'un fichier CSV décrivant la liste
	 * d'adjacence du graphe.
	 */
	public void importFromCSV(String path) {
		final Class vertexClass = this.vertexClass;
		final Class edgeClass = this.edgeClass;
		VertexProvider<T1> vprov = new VertexProvider<T1>()	{
			@Override
			public T1 buildVertex(String label, Map<String, Attribute> attributes) {
				try {
					return (T1) vertexClass.getDeclaredConstructor(int.class).newInstance(Integer.parseInt(label));
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				return null;
			}
		};

		EdgeProvider<T1,T2> eprov = new EdgeProvider<T1, T2>() {
			@Override
			public T2 buildEdge(T1 t1, T1 v1, String s, Map<String, Attribute> map) {
				try {
					return (T2) edgeClass.getDeclaredConstructor(Object.class,Object.class).newInstance(t1,v1);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				return null;
			}
		};

		CSVImporter<T1,T2> importer = new CSVImporter<T1,T2>(vprov,eprov);
		try {
			importer.importGraph(this,new File(path));
		} catch (ImportException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Exporte le graphe sous forme d'un fichier CSV qui décrit la liste
	 * d'adjacence du graphe.
	 */
	public String exportToCSV(String path) {
		ComponentNameProvider<T1> nprov = new ComponentNameProvider<T1>() {
			@Override
			public String getName(T1 t1) {
				int label = ((AbstractNode) t1).getLabel();
				return Integer.toString(label);
			}
		};

		CSVExporter<T1,T2> exporter = new CSVExporter<T1, T2>();
		exporter.setVertexIDProvider(nprov);
		try {
			exporter.exportGraph(this,new File(path));
		} catch (ExportException e) {
			e.printStackTrace();
		}
		return path;
	}
}
