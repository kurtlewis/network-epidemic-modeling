// Graph Stream imports
import org.graphstream.graph.Node;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.algorithm.generator.FullGenerator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.stream.file.FileSinkImages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        // initialize objects
        susceptible = new HashSet<>();
        infected = new HashSet<>();
        immune = new HashSet<>();
        graph = new SingleGraph("Epidemic Model");
        graph.addAttribute("ui.stylesheet", " node { size: 20px; } node.susceptible { fill-color: black; } "
                                    + "node.infected { fill-color: tomato; } node.immune { fill-color: green; } "
                                    + "edge { fill-color: darkgray; }");
        graph.addAttribute("ui.quality");
        graph.display();

        //
        // Graph generation
        //
        Generator gen = null;
        // choose the generator
        if (generator.equals("BarabasiAlbert")) {
            gen = new BarabasiAlbertGenerator();
        } else if (generator.equals("DorogovtsevMendes")) {
            gen = new DorogovtsevMendesGenerator();
        } else if (generator.equals("FullyConnected")) {
            gen = new FullGenerator();
        } else if (generator.equals("Grid")) {
            gen = new GridGenerator();
        } else if (generator.equals("Random")) {
            gen = new RandomGenerator();
        }
        // generate the graph
        gen.addSink(graph);
        gen.begin();
        for (int idx = 0; idx < numNodes; idx++) {
            gen.nextEvents();
        }
        gen.end();

        // copy nodes to susceptible set
        for (Node n : graph.getEachNode()) {
            susceptible.add(n);
        }
    }

    /**
     * Takes a neighbors file and builds it into a graph
     * Deprecated
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
    public void run(int iterations, String outputFilePrefix) {
        //
        // Prepare to save to images
        //
        FileSinkImages.OutputPolicy outputPolicy = FileSinkImages.OutputPolicy.BY_STEP;
        FileSinkImages.OutputType outputType = FileSinkImages.OutputType.PNG;
        String extension = ".png";
        FileSinkImages.Resolution resolution = FileSinkImages.Resolutions.HD720;
        FileSinkImages fsImages = new FileSinkImages(outputFilePrefix, outputType, resolution, outputPolicy);
        fsImages.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
        fsImages.setQuality(FileSinkImages.Quality.HIGH);
        /**
         * A note on FileSinkImages and how you're supposed to write images
         ******************************************************************
         * There is supposed to be a way for writing images when the graph marks a step. I have everything working,
         * except the generated images are blank. Due to a lack of documentation and online help, I've decided to write
         * the code myself using a utility method. I've left the autoimage generation code below commented out in case I
         * ever figure it out.
         *
         * On top of that, FileSinkImages has the functionality to write individual screenshots, and that doesn't work
         * because there is no way to reuse a previously generated layout - it is always calculated at the time of a new image
         * So, i used the backdoor of using the "ui.screenshot" attribute on graphs. I commented out the try/except code below
         * but am also leaving it, in case I come back to do this the right way, if I ever figure out how that is.
         */
        //graph.clearSinks();
        //graph.addSink(fsImages);
        // kick off recording
        //try {
            //fsImages.begin(outputFilePrefix);
            // code to run the visualization
            for (int idx = 0; idx < iterations; idx++) {
                //
                // Write the image
                //
                // Generate the filename
                int numZeros = Integer.toString(iterations).length() - Integer.toString(idx).length();
                StringBuilder filename = new StringBuilder();
                filename.append(outputFilePrefix);
                for (int jdx = 0; jdx < numZeros; jdx++) {
                    filename.append('0');
                }
                filename.append(idx);
                filename.append(extension);
                // Actually write the image
                //fsImages.writeAll(graph, filename.toString());
                graph.addAttribute("ui.screenshot", filename.toString());
                //
                // Do Step
                //
                graph.stepBegins(idx);
                step();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            //fsImages.end();
        //} catch (IOException e) {
            //System.out.println(e);
        //}

    }

    // Implement this for individual steps of the epidemic model
    abstract void step();

}
