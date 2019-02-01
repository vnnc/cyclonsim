package models;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.io.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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


	public void generateRandom(int vertexAmount,double edgeProba) {
		GnpRandomGraphGenerator<T1, T2> rgg = new GnpRandomGraphGenerator<T1, T2>(vertexAmount, edgeProba);
		VertexFactory<T1> factory = new VertexFactory<T1>() {
			private int n = 0;
			public T1 createVertex() {
				T1 node = null;
				try {
					node = (T1) vertexClass.getDeclaredConstructor(int.class).newInstance(n);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				n++;
				return (T1) node;
			}
		};
		rgg.generateGraph(this, factory,null);
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

	public ArrayList<T1> getNeighborsOfNode(T1 node) { // XXX
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

	public void importFromCSV(String path)
	{
		final Class vertexClass = this.vertexClass;
		final Class edgeClass = this.edgeClass;
		VertexProvider<T1> vprov = new VertexProvider<T1>()
		{
			@Override
			public T1 buildVertex(String label, Map<String, Attribute> attributes)
			{
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

	public String exportToCSV(String path)
	{
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
