import models.Edge;
import models.Graph;
import models.NodeTest;

import javax.xml.soap.Node;
import java.util.Iterator;
import java.util.Set;

public class Main {

    public static void main(String args[])
    {
        System.out.println("Running");
        Graph G = new Graph(Edge.class);
        G.addVertex(new NodeTest(1));
        G.addVertex(new NodeTest(2));
        Set<NodeTest> set = G.vertexSet();
        Iterator<NodeTest> setiter = set.iterator();
        while(setiter.hasNext())
        {
            if(setiter.next().getLabel() == 2)
            {
                System.out.println("Found vertex 2");
            }
        }
    }
}
