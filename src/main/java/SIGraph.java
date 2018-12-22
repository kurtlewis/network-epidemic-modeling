import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Random;

import org.graphstream.graph.Node;

public class SIGraph extends AbstractEpidemicGraph {
    private double beta;
    private Random rGen = new Random();
    public SIGraph(String generator, int numNodes, double beta) {
        super(generator, numNodes);
        this.beta = beta;
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
        // iterate through already infected nodes and find new infections
        // add them to the set to be later marked as infected
        for (Node node : infected) {
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
    }
}
