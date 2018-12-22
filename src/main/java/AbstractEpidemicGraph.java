// Graph Stream imports
import org.graphstream.graph.Node;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
/**
 * @author Kurt Lewis
 * This class is an abstract implementation of an epidemic model
 */
public abstract class AbstractEpidemicGraph {
    protected Set<Node> susceptible;
    protected Set<Node> infected;
    protected Set<Node> immune;
    protected Graph graph;

    public AbstractEpidemicGraph(String generator, int numNodes) {
        susceptible = new HashSet<>();
        infected = new HashSet<>();
        immune = new HashSet<>();
        graph = new SingleGraph("Epidemic Model");
        graph.addAttribute("ui.stylesheet", " node { size: 20px; } node.susceptible { fill-color: black; } "
                                    + "node.infected { fill-color: tomato; } node.immune { fill-color: green; } "
                                    + "edge { fill-color: darkgray; }");
        graph.addAttribute("ui.quality");
        graph.display();
        if (generator.equals("BarabasiAlbert")) {
            generateBarabasiAlbertGraph(numNodes);
        }
        // copy nodes to susceptible set
        for (Node n : graph.getEachNode()) {
            susceptible.add(n);
        }
    }

    private void generateBarabasiAlbertGraph(int numNodes) {
        Generator gen = new BarabasiAlbertGenerator();
        gen.addSink(graph);
        gen.begin();
        for (int idx = 0; idx < numNodes; idx++) {
            gen.nextEvents();
        }
        gen.end();
    }

    /**
     * Takes a neighbors file and builds it into a graph
     * Reminder: neighbors file has a line for every node in the graph, and the first node on a line has an edge to every other node on that line
     * @param filename - neighbors file to read
     */
    private void buildGraph(String filename) {
        File inFile = new File(filename);
        try {
            Scanner in = new Scanner(inFile);
            //
            // Iterate through lines in neighbor file and build graph
            //
            while (in.hasNext()) {
                String line = in.nextLine();
                // split on spaces to get node names
                String[] nodes = line.split(" ");
                // make sure there are nodes in the list
                if (nodes.length > 0) {
                    String node = nodes[0];
                    // add the node to the graph if it doesn't exist
                    if (graph.getNode(node) == null) {
                        graph.addNode(node);
                        Node newNode = graph.getNode(node);
                        newNode.setAttribute("ui.class", "susceptible");
                    }
                    susceptible.add(graph.getNode(node));
                    for (int idx = 1; idx < nodes.length; idx++) {
                        // add the node to the graph if it doesn't exist
                        if (graph.getNode(nodes[idx]) == null) {
                            graph.addNode(nodes[idx]);
                            Node newNode = graph.getNode(nodes[idx]);
                            newNode.setAttribute("ui.class", "susceptible");

                        }
                        // determine the ID so that the lower valued node is first in the concatenation
                        String id = null;
                        if (node.compareTo(nodes[idx]) <= 0) {
                            id = node + nodes[idx];
                        } else {
                            id = nodes[idx] + node;
                        }
                        // add the edge if it doesn't exist
                        if (graph.getEdge(id) == null) {
                            graph.addEdge(id, node, nodes[idx]);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    /**
     * Runs and saves the epidemic model
     * @param iterations - the numbr of iterations the epidemic should run for
     */
    public void run(int iterations) {
        //graph.display();
        // code to run the visualization
        for (int idx = 0; idx < iterations; idx++) {
            step();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    // Implement this for individual steps of the epidemic model
    abstract void step();

}
