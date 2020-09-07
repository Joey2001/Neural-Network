public class Layer {
    Neuron[] neuronStack;
    Functions functions = new Functions();

    Layer(int stackSize){
        neuronStack = new Neuron[stackSize];
        for(int neuron = 0; neuron < neuronStack.length; neuron++)
            neuronStack[neuron] = new Neuron();
    }

    public float[] getData(){
        float[] data = new float[neuronStack.length];
        for(int neuron = 0; neuron < neuronStack.length; neuron++)
            data[neuron] = neuronStack[neuron].getData();
        return data;
    }

    public void setData(float[] data){
        for(int neuron = 0; neuron < neuronStack.length; neuron++)
            neuronStack[neuron].setData(data[neuron]);
    }

    public float[] getError(){
        float[] error = new float[neuronStack.length];
        for(int neuron = 0; neuron < error.length; neuron++)
            error[neuron] = neuronStack[neuron].getError();
        return error;
    }

    public float getSynapse(int location, int synapseLocation){
        return neuronStack[location].getSynapse(synapseLocation);
    }

    public activation getActivation(){
        return neuronStack[0].getActivation();
    }

    public void setActivation(activation function){
        for (Neuron neuron : neuronStack)
            neuron.setActivation(function);
    }

    public void setRandomizedBias(float[] searchBounds){
        for (Neuron neuron : neuronStack)
            neuron.setBias(functions.getRandomFloat(searchBounds));
    }

    public void setRandomizedWeights(int nextStackSize, float[] searchBounds){
        for (Neuron neuron : neuronStack)
            for (int nextNeuron = 0; nextNeuron < nextStackSize; nextNeuron++)
                neuron.setSynapse(nextNeuron, functions.getRandomFloat(searchBounds));
    }

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

    public void setError(float[] target){
        for(int neuron = 0; neuron < neuronStack.length; neuron++){
            float weight = neuronStack[neuron].getData() - target[neuron];
            float error = functions.getValue(getActivation(), neuronStack[neuron].getData(), true) * weight;
            neuronStack[neuron].setError(error);
        }
    }

    public int size(){
        return neuronStack.length;
    }

    public float largestSynapse(){
        float bigNumber = 0f;
        for (Neuron neuron : neuronStack) {
            float largeNumber = neuron.largestSynapse();
            bigNumber = Math.max(bigNumber, largeNumber);
        }
        return bigNumber;
    }

    public void pruneLayer(float threshold) {
        for (Neuron neuron : neuronStack)
            neuron.pruneSynapses(threshold);
    }

    public void reconstructLayer(Neuron[] neuronStack){
        this.neuronStack = neuronStack;
    }

    @Override
    public String toString() {
        StringBuilder layerString = new StringBuilder();
        for(Neuron neuron : neuronStack)
            layerString.append(neuron.toString());
        return layerString.toString() + "L";
    }
}