import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        int[] layers = {2, 2, 1};
        String saveDestination = "*Neural Network text file location*";
        NeuralNetwork network = new NeuralNetwork(layers, saveDestination);

        DataSets trainingData = new DataSets();

        trainingData.addDataSet(new float[]{0.2f, 0.2f}, new float[]{0.4f});
        trainingData.addDataSet(new float[]{0.3f, 0.4f}, new float[]{0.7f});
        trainingData.addDataSet(new float[]{0.7f, 0.1f}, new float[]{0.8f});

        System.out.println(Arrays.toString(network.predict(new float[]{0.2f, 0.2f})));
        System.out.println(Arrays.toString(network.predict(new float[]{0.3f, 0.4f})));
        System.out.println(Arrays.toString(network.predict(new float[]{0.3f, 0.2f})));

        network.train(trainingData, 2800, .1f, .008f);

        System.out.println(Arrays.toString(network.predict(new float[]{0.2f, 0.2f})));
        System.out.println(Arrays.toString(network.predict(new float[]{0.3f, 0.4f})));
        System.out.println(Arrays.toString(network.predict(new float[]{0.3f, 0.2f})));

        NeuralNetwork newNetwork = new NeuralNetwork(saveDestination);
        System.out.println("RECONSTRUCTED BRAIN");
        System.out.println(Arrays.toString(newNetwork.predict(new float[]{0.2f, 0.2f})));
        System.out.println(Arrays.toString(newNetwork.predict(new float[]{0.3f, 0.4f})));
        System.out.println(Arrays.toString(newNetwork.predict(new float[]{0.3f, 0.2f})));
    }
}
