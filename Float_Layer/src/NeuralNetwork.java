import java.io.*;
import java.util.ArrayList;

public class NeuralNetwork {
    Layer[] brain;
    String saveDestination;
    ArrayList<Float> deltaError;

//    takes in an array as the parameter for the network size and a string to save the brain in a text file
    NeuralNetwork(int[] layerSizes, String saveDestination){
        this(layerSizes, new float[]{-1f, 1f}, saveDestination);
    }

    NeuralNetwork(int[] layerSizes, float[] searchBounds, String saveDestination){
        brain = new Layer[layerSizes.length];
        deltaError = new ArrayList<>();
        this.saveDestination = saveDestination;

        for(int layer = 0; layer < layerSizes.length; layer++){
            brain[layer] = new Layer(layerSizes[layer]);

            if(layer > 0)
                brain[layer - 1].setRandomizedWeights(brain[layer].size(), searchBounds);

            brain[layer].setRandomizedBias(searchBounds);
        }
        brain[brain.length - 1].setActivation(activation.LEAKY_REC_LIN);
    }

//    constructor to pull the brain from the text file
    public NeuralNetwork(String location) {
        this.brain = new ReconnectBrain(location).reconstruct();
    }

//    takes input and 'guesses' what the output should be
    public float[] predict(float[] input) throws Exception{
        if(input.length != brain[0].size())
            throw new Exception("Data does not match input size");

        brain[0].setData(input);
        for(int layer = 1; layer < brain.length; layer++)
            brain[layer].predict(brain[layer - 1]);

        return brain[brain.length - 1].getData();
    }

//    iterates through all of the layers and calculates error based on the next layer's error
//    calculating error is done from end to start
    private float[] findError(float[] target){
        brain[brain.length - 1].setError(target);

        for(int layer = brain.length - 2; layer >= 0; layer--)
            brain[layer].backPropagation(brain[layer + 1]);

        return brain[brain.length - 1].getError();
    }

//    finds the synapse with the largest strength value and scales the threshold value
//    then finds all synapses with a strength value less than the weighted threshold value
    private void pruneNetwork(float threshold){
        float bigNumber = 0f;
        for (Layer layer : brain) {
            float largeNumber = layer.largestSynapse();
            bigNumber = Math.max(bigNumber, largeNumber);
        }

        for(int layer = 0; layer < brain.length - 1; layer++)
            brain[layer].pruneLayer(bigNumber * threshold);
    }

//    trains the network in multiple phases, the last section of training is reserved to train after pruning
//    public interface to input the necessary parameters
    public void train(DataSets trainingData, int iterations, float pruningThreshold, float defaultLearningRate) throws Exception{
        iterations *= (trainingData.size() * .5);
        for(int trainingSection = 0; trainingSection < 2; trainingSection++){
            for(int iteration = 0; iteration < iterations; iteration++){
                int index = iteration % trainingData.size();
                boolean fixedRate = (trainingSection != 0) && .8 * iteration > iterations;
                trainNeuralNetwork(trainingData.getDataSet(index)[0], trainingData.getDataSet(index)[1], defaultLearningRate, fixedRate);
            }
            if(trainingSection == 0)
                pruneNetwork(pruningThreshold);
            else
                saveBrain();
        }
    }

//    class that calls all necessary classes for training
    private void trainNeuralNetwork(float[] input, float[] target, float learningRate, boolean fixedEnd) throws Exception {
        predict(input);

        float[] findError = findError(target);
        float error = 0;

        for(float errors : findError)
            error += errors;

        deltaError.add(error);
        if(deltaError.size() > 3) deltaError.remove(0);

        float adaptiveLearningRate = (fixedEnd || deltaError.size() <= 2) ? learningRate : AdaptiveLearningRate(deltaError);

        for(int layer = 0; layer < brain.length - 1; layer++)
            brain[layer].changeWeights(adaptiveLearningRate, brain[layer + 1]);
    }

//    uses numerical differentiation to approximate the rate error is changing
    private float AdaptiveLearningRate(ArrayList<Float> deltaError) {
        float x = (-(deltaError.get(2)) + 4*(deltaError.get(1)) - 3*(deltaError.get(0)))/2;
        return (float) ((float) (Math.exp(-(x * x)/2))/(2 * Math.sqrt(Math.PI)));
    }

//    saves the neural network structure in a text file
    private void saveBrain() {
        StringBuilder brainString = new StringBuilder();
        for (Layer layer : this.brain)
            brainString.append(layer.toString());

        try(FileWriter fileWriter = new FileWriter(saveDestination)){
            fileWriter.write(brainString.toString());
        } catch(IOException ignored){
        }
    }
}