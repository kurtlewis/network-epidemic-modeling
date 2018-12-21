import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Random;

import org.graphstream.graph.Node;

public class SISGraph extends AbstractEpidemicGraph {
    private double beta;
    private double mu;
    private Random rGen = new Random();

    public SISGraph(String filename, double beta, double mu) {
        super(filename);
        this.beta = beta;
        this.mu = mu;
        int rand = rGen.nextInt(susceptible.size());
        // removing a random element from a hashset is slow
        int idx = 0;
        for (Node n : susceptible) {
            if (idx == rand) {
                infected.add(n);
                susceptible.remove(n);
                break;
            }
            idx++;
        }
    }

    @Override
    public void step() {
        Set<Node> toBeInfected = new HashSet();
        Set<Node> toRecover = new HashSet();
        // iterate through already infected nodes and find new infections, and if those nodes recover
        // add them to the set to be later marked as infected
        for (Node node : infected) {
            // check fo recovery before infections
            if (this.rGen.nextDouble() < this.mu) {
                toRecover.add(node);
                // this node has recovered, so don't infect anything new
                continue;
            }
            Iterator<Node> neighborsIter = node.getNeighborNodeIterator();
            while (neighborsIter.hasNext()) {
                Node neighbor = neighborsIter.next();
                if (this.rGen.nextDouble() < this.beta) {
                    toBeInfected.add(neighbor);
                }
            }
        }
        // iterate over nodes to be infected and switch them, then mark them
        for (Node node : toBeInfected) {
            // Switch the set membership
            susceptible.remove(node);
            infected.add(node);
            node.setAttribute("ui.class", "infected");
        }
        // iterate over healthy nodes and move them to the right sets
        for (Node node : toRecover) {
            // Switch set membership
            infected.remove(node);
            susceptible.add(node);

            node.setAttribute("ui.class", "susceptible");
        }
    }
}
