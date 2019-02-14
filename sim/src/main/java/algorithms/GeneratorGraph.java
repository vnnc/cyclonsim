package algorithms;
import java.util.*;

import junit.framework.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;


public class GeneratorGraph extends AbstractAlgorithm {


	private Graph<SimpleNode, SimpleEdge> graph;
	private Random rng = new Random();
	private int graphSize;


	@Override
	public void initRandomGraph(int graphSize, int cacheSize, int shuffleLength) {
		this.graphSize = graphSize;
	}

	@Override
	public void initGraphFromCSV(String path, int cacheSize, int shuffleLength) {
		//TODO ??
	}

	@Override
	public void initGraph(Graph g, int cacheSize,int shuffleLength) {
		this.graphSize = g.vertexSet().size();
		this.graph = g;
	}

	@Override
	public void shuffleAll() {}

	@Override
	public AbstractNode nextPeer(Integer label){
		AbstractNode node = new SimpleNode(rng.nextInt(this.graphSize));
		return node;
	}

	@Override
	public Graph getGraph() {
		return this.graph;
	}
	public void initGraph( int n, int maxEdge ){
		this.graph = new Graph<SimpleNode,SimpleEdge>(SimpleNode.class,SimpleEdge.class);


//On remplit notre graph avec des random number
		for (int i = 0; i < n; i++) {


if()
			this.grah.addEdge(v1, v2)
		}

		for (int i = 0; i < n; i++) {
			this.graph.addVertex(i);
			for (int j = 0; j < i; j++) {
				int v1 = rng.nextInt(size);


				if(i==v1 && v1==0){
					v1= v1+1;

					this.grah.addEdge(i,v1);

				}else if(i==v1){
                      v=1 v1-1;
					this.grah.addEdge(i, v1);

				}

			}
		}
//on va  bien que chaque vertices  le graph n'a pas d'edges plus grand maxEdges
		for (int i = 0; i < n; i++){

		while (this.graph.degreeOf(i)< = maxEdge){

			this.graph.removeEdge(i,rng.nextInt(n))
		};
		}
		System.out.println("Graph :"+this.graph);

	}


}
