public class Main {

    public static void main(String[] args) {
        if (args.length < 5 || args[0].equals("h")) {
            System.out.println("Usage Instructions:");
            System.out.println("Java Main <neighbors file> <out file for saved video> <model> <iterations> <beta>");
            System.out.println("Models: 'SI', 'SIS', 'SRS'");
            System.exit(0);
        }
        String inFile = args[0];
        String outFile = args[1];
        String model = args[2];
        int iterations = Integer.parseInt(args[3]);
        double beta = Double.parseDouble(args[4]);
        AbstractEpidemicGraph graph = null;
        if (model.equals("SI")) {
            graph = new SIGraph(inFile, beta);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        graph.run(2000);
    }
}
