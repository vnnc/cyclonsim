import models.EmptyNode;
import models.Graph;
import models.NodeTest;

public class Main {

    public static void main(String args[])
    {
        System.out.println("Running");
        Graph<NodeTest> G = new Graph<NodeTest>(NodeTest.class);

        G.generateRandom(5,8);

        System.out.println("Age of node 0: "+G.getNodeByLabel(0).getAge());
        System.out.println("Neighbors of node 0: "+G.getNeighborsOfNode(G.getNodeByLabel(0)));
        System.out.println(G);
    }
}
