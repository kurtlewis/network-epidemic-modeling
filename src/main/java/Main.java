public class Main {

    public static void main(String[] args) {
        if (args.length < 5 || args[0].equals("h")) {
            System.out.println("Usage Instructions:");
            System.out.println("Java Main <out file for saved video> <model> <generator> <numNodes> <iterations> <beta> <mu>");
            System.out.println("Models: 'SI', 'SIS', 'SRS'");
            System.exit(0);
        }
        String outFile = args[0];
        String model = args[1];
        String generator = args[2];
        int numNodes = Integer.parseInt(args[3]);
        int iterations = Integer.parseInt(args[4]);
        double beta = Double.parseDouble(args[5]);
        AbstractEpidemicGraph graph = null;
        if (model.equals("SI")) {
            graph = new SIGraph(generator, numNodes, beta);
        } else if (model.equals("SIS")) {
            double mu = Double.parseDouble(args[6]);
            graph = new SISGraph(generator, numNodes, beta, mu);
        } else if (model.equals("SIR")) {
            double mu = Double.parseDouble(args[6]);
            graph = new SIRGraph(generator, numNodes, beta, mu);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        graph.run(iterations, outFile);
        System.out.println("Complete.");
    }
}
