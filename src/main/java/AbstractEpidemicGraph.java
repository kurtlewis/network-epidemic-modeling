// Graph Stream imports
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
/**
 * @author Kurt Lewis
 * This class is an abstract implementation of an epidemic model
 */
public abstract class AbstractEpidemicGraph {
    protected Set<Node> healthy;
    protected Set<Node> infected;
    protected Set<Node> immune;
    protected SingleGraph graph;

    public AbstractEpidemicGraph(String filename) {
        healthy = new HashSet<>();
        infected = new HashSet<>();
        immune = new HashSet<>();
        graph = new SingleGraph("Epidemic Model");
        this.buildGraph(filename);
    }

    /**
     * Takes a neighbors file and builds it into a graph
     * Reminder: neighbors file has a line for every node in the graph, and the first node on a line has an edge to every other node on that line
     * @param filename - neighbors file to read
     */
    private void buildGraph(String filename) {
        Scanner in = new Scanner(filename);
        while (in.hasNext()) {
            String line = in.nextLine();
            // remove new line from input
            line = line.substring(0, line.length() - 2);
            // split on spaces to get
            String[] nodes = line.split(" ");
            if (nodes.length > 1) {
                String node = nodes[0];
                graph.addNode(node);
                for (int idx = 1; idx < nodes.length; idx++) {
                    graph.addNode(nodes[idx]);
                    graph.addEdge(node + nodes[idx], node, nodes[idx]);
                }
            }
        }
    }

    /**
     * Runs and saves the epidemic model
     * @param iterations - the numbr of iterations the epidemic should run for
     */
    public void run(int iterations) {
        // code to run the visualization
        for (int idx = 0; idx < iterations; idx++) {
            step();
        }
    }

    // Implement this for individual steps of the epidemic model
    abstract void step();

}
