// Graph Stream imports
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Set;
import java.util.HashSet;
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

    private void buildGraph(String filename) {

    }

    // will run and save the epidemic model
    public void run() {
        // code to run the visualization
    }

    // Implement this for individual steps of the epidemic model
    abstract void step();

}
