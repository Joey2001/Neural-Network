import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        int[] layers = {2, 3, 1};
        String saveDestination = "**SET TO LOCATION OF TEXT FILE**";
        NeuralNetwork network = new NeuralNetwork(layers, saveDestination);

//        initializing class to store training data
        DataSets trainingData = new DataSets();

        float[] train1 = {0.2f, 0.2f};
        float[] train2 = {0.3f, 0.4f};
        float[] train3 = {0.7f, 0.1f};

        float[] test2 = {0.3f, 0.4f};
        float[] test3 = {0.3f, 0.2f};

//        adds data to data set
        trainingData.addDataSet(train1, new float[]{0.4f});
        trainingData.addDataSet(train2, new float[]{0.7f});
        trainingData.addDataSet(train3, new float[]{0.8f});

        System.out.println(Arrays.toString(network.predict(train1)));
        System.out.println(Arrays.toString(network.predict(test2)));
        System.out.println(Arrays.toString(network.predict(test3)));

        network.train(trainingData, 3000, 0f, .008f);

        System.out.println(Arrays.toString(network.predict(train1)));
        System.out.println(Arrays.toString(network.predict(test2)));
        System.out.println(Arrays.toString(network.predict(test3)));

//        creates a new network and tests if the outputs match the trained brain
        NeuralNetwork newNetwork = new NeuralNetwork(saveDestination);
        System.out.println("RECONSTRUCTED BRAIN");
        System.out.println(Arrays.toString(newNetwork.predict(train1)));
        System.out.println(Arrays.toString(newNetwork.predict(test2)));
        System.out.println(Arrays.toString(newNetwork.predict(test3)));
    }
}