package algorithms;

import java.util.Iterator;
import java.util.Set;

import models.*;

public class AlgorithmTest extends AbstractAlgorithm {

	public void initGraph() {
		this.graph = new Graph(Edge.class);
		this.graph.addVertex(new NodeTest(1));
		this.graph.addVertex(new NodeTest(2));
	}
	
	public void shuffle() {
		Set<NodeTest> set = this.graph.vertexSet();
		Iterator<NodeTest> setiter = set.iterator();
		while(setiter.hasNext()) {
			if(setiter.next().getLabel() == 2) {
				System.out.println("Found vertex 2");
			}
		}
	}
	
	public void testKhi2() {
		// TODO
	}
	
	public AbstractNode nextPeer() {
		return new NodeTest(2);
	}
}
