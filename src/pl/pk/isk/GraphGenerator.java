package pl.pk.isk;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GraphGenerator {

    public DirectedSparseMultigraph<CustomNode, CustomLink> createNodesAndEdges() {
        DirectedSparseMultigraph<CustomNode, CustomLink> directedSparseMultigraph = new DirectedSparseMultigraph<CustomNode, CustomLink>();

        CustomNode n1 = new CustomNode(1);
        CustomNode n2 = new CustomNode(2);
        CustomNode n3 = new CustomNode(3);
        CustomNode n4 = new CustomNode(4);
        CustomNode n5 = new CustomNode(5);

        directedSparseMultigraph.addEdge(new CustomLink(2.0, 48), n1, n2, EdgeType.DIRECTED);
        directedSparseMultigraph.addEdge(new CustomLink(2.0, 48), n2, n3, EdgeType.DIRECTED);
        directedSparseMultigraph.addEdge(new CustomLink(3.0, 192), n3, n5, EdgeType.DIRECTED);
        directedSparseMultigraph.addEdge(new CustomLink(2.0, 48), n5, n4, EdgeType.DIRECTED);
        directedSparseMultigraph.addEdge(new CustomLink(2.0, 48), n4, n2);
        directedSparseMultigraph.addEdge(new CustomLink(2.0, 48), n3, n1);
        directedSparseMultigraph.addEdge(new CustomLink(10.0, 48), n2, n5);

        return directedSparseMultigraph;
    }

    public DirectedSparseMultigraph<CustomNode, CustomLink> generateGraph(int numberOfNodes, int numberOfEdgesBetweenNodes) {
        List<CustomNode> listOfNodes = new ArrayList<>(numberOfNodes);

        IntStream.rangeClosed(1, numberOfNodes).forEach(i -> listOfNodes.add(new CustomNode(i)));

        DirectedSparseMultigraph<CustomNode, CustomLink> directedSparseMultigraph = new DirectedSparseMultigraph<CustomNode, CustomLink>();
        Random random = new Random();
        listOfNodes.stream().forEach(node ->
                IntStream.rangeClosed(1,numberOfEdgesBetweenNodes).forEach(counter
                        -> directedSparseMultigraph.addEdge(new CustomLink((int)(random.nextDouble() * 100), random.nextInt(100)),
                            node, listOfNodes.get(random.nextInt(listOfNodes.size())))));

        return directedSparseMultigraph;
    }
}
