package algorithms;

import models.AbstractNode;
import models.SimpleEdge;
import models.Graph;
import models.SimpleNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AlgorithmBasic extends AbstractAlgorithm{

    private Graph<SimpleNode,SimpleEdge> graph;

    private Random rng = new Random();

    @Override
    public void initGraph() {
        this.graph = new Graph<SimpleNode,SimpleEdge>(SimpleNode.class,SimpleEdge.class);
        this.graph.generateRandom(5,0.2);
    }

    @Override
    public void shuffle(int nodeLabel,int shuffleLength) {
        System.out.println("Graph before shuffle: "+this.graph);

        SimpleNode node = this.graph.getNodeByLabel(nodeLabel);

        ArrayList<SimpleNode> nodeNeighbors = this.graph.getNeighborsOfNode(node);

        System.out.println("Neighbors of node "+node+": "+nodeNeighbors);

        int neighborsSubsetSize = (shuffleLength>nodeNeighbors.size()) ? nodeNeighbors.size() : shuffleLength;

        if(nodeNeighbors.size()>0)
        {
            ArrayList<SimpleNode> nodeNeighborsSubset = new ArrayList<SimpleNode>();
            nodeNeighborsSubset.addAll(nodeNeighbors);
            Collections.shuffle(nodeNeighborsSubset);
            System.out.println("DEBUG: nodeNeighborsSubset: "+nodeNeighborsSubset);
            nodeNeighborsSubset = new ArrayList<SimpleNode>(nodeNeighborsSubset.subList(0,neighborsSubsetSize));

            System.out.println("Subset of neighbors: "+nodeNeighborsSubset);

            SimpleNode peer = nodeNeighborsSubset.get(rng.nextInt(nodeNeighborsSubset.size()));
            peer = this.graph.getNodeByLabel(peer.getLabel());

            System.out.println("Chosen peer: "+peer);

            ArrayList<SimpleNode> subsetForPeer = new ArrayList<SimpleNode>();
            for(SimpleNode n : nodeNeighborsSubset)
            {
                if(n.getLabel() == peer.getLabel())
                {
                    subsetForPeer.add(node);
                }else{
                    subsetForPeer.add(n);
                }
            }

            System.out.println("Subset sent to peer: "+subsetForPeer);

            ArrayList<SimpleNode> peerNeighbors = this.graph.getNeighborsOfNode(peer);

            System.out.println("Peer neighbors: "+peerNeighbors);

            int peerNeighborsSubsetSize = (shuffleLength>peerNeighbors.size()) ? peerNeighbors.size() : shuffleLength;

            if(peerNeighbors.size()>0)
            {
                ArrayList<SimpleNode> peerNeighborsSubset = new ArrayList<SimpleNode>();
                peerNeighborsSubset.addAll(peerNeighbors);
                Collections.shuffle(peerNeighborsSubset);
                System.out.println("DEBUG: peerNeighborsSubset: "+peerNeighborsSubset);
                peerNeighborsSubset = new ArrayList<SimpleNode>(peerNeighborsSubset.subList(0,peerNeighborsSubsetSize));

                System.out.println("Subset of peer neighbors: "+peerNeighborsSubset);

                ArrayList<SimpleEdge> newEdges = new ArrayList<SimpleEdge>();

                ArrayList<SimpleNode> nodeKeptEntries = new ArrayList<SimpleNode>();

                for(SimpleNode n : peerNeighborsSubset)
                {
                    n = this.graph.getNodeByLabel(n.getLabel());
                    if(this.graph.getEdge(n,node)==null && !nodeNeighbors.contains(n) && n.getLabel()!=nodeLabel)
                    {
                        nodeKeptEntries.add(n);
                        this.graph.addEdge(node,n);
                        newEdges.add(this.graph.getEdge(node,n));
                    }
                }

                System.out.println("Remaining entries within subset from peer: "+nodeKeptEntries);

                ArrayList<SimpleNode> peerKeptEntries = new ArrayList<SimpleNode>();

                for(SimpleNode n : subsetForPeer)
                {
                    n = this.graph.getNodeByLabel(n.getLabel());
                    if(this.graph.getEdge(n,peer)==null && !peerNeighbors.contains(n) && n.getLabel()!=peer.getLabel())
                    {
                        peerKeptEntries.add(n);
                        this.graph.addEdge(peer,n);
                        newEdges.add(this.graph.getEdge(peer,n));
                    }
                }

                System.out.println("Remaining entries within subset from node: "+peerKeptEntries);

                System.out.println("Created Edges: "+newEdges);

            }
        }
        System.out.println("Graph after shuffle: "+this.graph);

    }

    @Override
    public AbstractNode nextPeer() {
        return null;
    }
}
