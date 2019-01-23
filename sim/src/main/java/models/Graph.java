package models;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

public class Graph<T1> extends SimpleDirectedGraph<T1, Edge> {

    Class clazz;

    public Graph(Class clazz)
    {
        super(new EdgeFactory<T1, Edge>() {
            @Override
            public Edge createEdge(T1 t1, T1 v1) {
                return new Edge(t1,v1);
            }
        });
        this.clazz = clazz;

    }

    public void generateRandom(int vertexAmount,int edgeAmount)
    {
        RandomGraphGenerator<T1, Edge> rgg = new RandomGraphGenerator<T1,Edge>(vertexAmount,edgeAmount);
        VertexFactory<T1> factory = new VertexFactory<T1>() {

            private int n = 0;

            public T1 createVertex() {
                T1 node = null;
                try {
                    node = (T1) clazz.getDeclaredConstructor(int.class).newInstance(n);
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

    public T1 getNodeByLabel(int label)
    {
        Set<T1> nodes = this.vertexSet();
        Iterator<T1> niter = nodes.iterator();
        while(niter.hasNext())
        {
            AbstractNode next = (AbstractNode) niter.next();
            if(next.getLabel() == label)
            {
                return niter.next();
            }
        }

        return (T1) new NodeTest(0);
    }
}
