public class Main {

    public static void main(String[] args) {
        if (args.length < 6 || args[1].equals("h")) {
            System.out.println("Usage Instructions:");
            System.out.println("Java Main <neighbors file> <out file for saved video> <model> <iterations> <mu>");
            System.out.println("Models: 'SI', 'SIS', 'SRS'");
            System.exit(0);
        }
        String inFile = args[1];
        String outFile = args[2];
        String model = args[3];
        int iterations = Integer.parseInt(args[4]);
        double mu = Double.parseDouble(args[5]);
        AbstractEpidemicGraph graph;
        if (model.equals("SI")) {

        }
    }
}
