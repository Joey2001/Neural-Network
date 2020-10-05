public class Layer {
    Neuron[] neuronStack;
    Functions functions = new Functions();

    Layer(int stackSize){
        neuronStack = new Neuron[stackSize];
        for(int neuron = 0; neuron < neuronStack.length; neuron++)
            neuronStack[neuron] = new Neuron();
    }

//    this is used exclusively in ReconnectBrain to pass the recreated neuron stack to layer
    Layer(Neuron[] neuronStack){
        this.neuronStack = neuronStack;
    }

//    collects the data from all neurons in the stack and returns data from the stack in an array
    public float[] getData(){
        float[] data = new float[neuronStack.length];

        for(int neuron = 0; neuron < neuronStack.length; neuron++)
            data[neuron] = neuronStack[neuron].getData();
        return data;
    }

//    takes in an array and writes over the data in the neuron in the stack with the new data
    public void setData(float[] data){
        for(int neuron = 0; neuron < neuronStack.length; neuron++)
            neuronStack[neuron].setData(data[neuron]);
    }

//    collects the error from each neuron in the stack and returns error from the stack in an array
    public float[] getError(){
        float[] error = new float[neuronStack.length];
        for(int neuron = 0; neuron < error.length; neuron++)
            error[neuron] = neuronStack[neuron].getError();
        return error;
    }

//    takes in an array and writes over the error in the neuron in the stack with the new error
    public void setError(float[] target){
        for(int neuron = 0; neuron < neuronStack.length; neuron++){
            float weight = neuronStack[neuron].getData() - target[neuron];
            float error = functions.getValue(getActivation(), neuronStack[neuron].getData(), true) * weight;
            neuronStack[neuron].setError(error);
        }
    }

//    gets a specific connection to another neuron and returns the weight value
    public float getSynapse(int location, int synapseLocation){
        return neuronStack[location].getSynapse(synapseLocation);
    }

//    returns the activation function of the stack; it doesn't matter where in the stack
//    the activation is retrieved because the entire stack has the same function
    public activation getActivation(){
        return neuronStack[0].getActivation();
    }

//    sets the activation function of the stack
    public void setActivation(activation function){
        for (Neuron neuron : neuronStack)
            neuron.setActivation(function);
    }

//    retrieves a random value from the functions class and sets the bias to the value
    public void setRandomizedBias(float[] searchBounds){
        for (Neuron neuron : neuronStack)
            neuron.setBias(functions.getRandomFloat(searchBounds));
    }

//    sets the weights of the synapses to be randomized, allowing each weight to be changed
//    differently as training occurs
    public void setRandomizedWeights(int nextStackSize, float[] searchBounds){
        for (Neuron neuron : neuronStack)
            for (int nextNeuron = 0; nextNeuron < nextStackSize; nextNeuron++)
                neuron.setSynapse(nextNeuron, functions.getRandomFloat(searchBounds));
    }

//    changes the weights of the synapses based on the weight of the connection, error from
//    the next neuron stack (previous neuron stack relative to back propagation), and
//    the current neuron data
    public void changeWeights(float learningRate, Layer nextLayer){
        float[] nextLayerError = nextLayer.getError();
        for (Neuron neuron : neuronStack) {
            for (int nextNeuron = 0; nextNeuron < nextLayer.size(); nextNeuron++) {
                float delta = -learningRate * neuron.getData() * nextLayerError[nextNeuron];
                float synapseStrength = neuron.getSynapse(nextNeuron);

                if (Math.abs(synapseStrength) > 0)
                    neuron.setSynapse(nextNeuron, synapseStrength + delta);
            }
        }
    }

//    Feeds the data from the previous layer to calculate what the data
//    should be in the current layer
    public void predict(Layer prevLayer){
        float[] normalizedData = new float[size()];
        for(int neuron = 0; neuron < neuronStack.length; neuron++){
            float carryData = neuronStack[neuron].getBias();

            for(int prevNeuron = 0; prevNeuron < prevLayer.size(); prevNeuron++){
                float synapseStrength = prevLayer.getSynapse(prevNeuron, neuron);
                carryData += synapseStrength * prevLayer.getData()[prevNeuron];
            }
            normalizedData[neuron] = functions.getValue(getActivation(), carryData, false);
        }
        setData(normalizedData);
    }

//    calculates what the error should be in the current layer based on the next layer
//    (back propagation is hard to explain from a programming stand point on what is happening;
//     the error of the last layer is first calculated and is then carried towards the front)
    public void backPropagation(Layer nextLayer){
        float[] sum = new float[neuronStack.length];
        float[] nextError = nextLayer.getError();
        for(int neuron = 0; neuron < neuronStack.length; neuron++){
            for(int nextNeuron = 0; nextNeuron < nextLayer.size(); nextNeuron++){

                float connection = neuronStack[neuron].getSynapse(nextNeuron);
                sum[neuron] += nextError[nextNeuron] * connection;
            }
        }

        for(int neuron = 0; neuron < neuronStack.length; neuron++){
            float weight = sum[neuron];
            float error = functions.getValue(getActivation(), neuronStack[neuron].getData(), true) * weight;
            neuronStack[neuron].setError(error);
        }
    }

//    returns the size of the current stack; useful to get the size of other layer instances
    public int size(){
        return neuronStack.length;
    }

//    finds the synapse with the largest weight in the stack
    public float largestSynapse(){
        float bigNumber = 0f;
        for (Neuron neuron : neuronStack) {
            float largeNumber = neuron.largestSynapse();
            bigNumber = Math.max(bigNumber, largeNumber);
        }
        return bigNumber;
    }

//    prunes the least significant connections, and sets the weight to zero
    public void pruneLayer(float threshold) {
        for (Neuron neuron : neuronStack)
            neuron.pruneSynapses(threshold);
    }

    @Override
    public String toString() {
        String layerString = "";
        for(Neuron neuron : neuronStack)
            layerString += neuron.toString();
        return layerString + "L";
    }
}