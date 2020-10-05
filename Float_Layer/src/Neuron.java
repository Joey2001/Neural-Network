import java.util.*;

public class Neuron {
    private float[] attributes;
    private Map<Integer, Float> synapses;
    private activation function;

//    initializes all variables necessary for the neuron
    Neuron(){
        attributes = new float[3];
        synapses = new HashMap<>();
        function = activation.SIGMOID;
    }

//    returns the data stored in the neuron
    public float getData(){
        return attributes[0];
    }

//    sets the data in the neuron
    public void setData(float data){
        attributes[0] = data;
    }

//    returns the error stored in the neuron
    public float getError(){
        return attributes[1];
    }

//    sets the error in the neuron
    public void setError(float error){
        attributes[1] = error;
    }

//    returns the bias in the neuron
    public float getBias(){
        return attributes[2];
    }

//    sets teh bias in the neuron
    public void setBias(float bias){
        attributes[2] = bias;
    }

//    gets the synapse at a particular location and returns the weight of the synapse
    public float getSynapse(int location){
        return synapses.get(location);
    }

//    sets a synapse at a particular location to a particular strength
    public void setSynapse(int location, float strength){
        synapses.remove(location);
        synapses.put(location, strength);
    }

//    returns the activation function of the neuron
    public activation getActivation(){
        return function;
    }

//    changes the activation function of the neuron
    public void setActivation(activation function){
        this.function = function;
    }

//    returns the synapse with the largest weight
    public float largestSynapse() {
        float bigNumber = 0f;
        for(int number : synapses.keySet())
            bigNumber = Math.max(synapses.get(number), bigNumber);
        return bigNumber;
    }

//    checks if all synapses meet a threshold, otherwise the synapse is set to zero
    public void pruneSynapses(float threshold){
        for(int key = 0; key < synapses.size(); key++)
            if(Math.abs(synapses.get(key)) < threshold)
                setSynapse(key, 0f);
    }

//    puts the synapse list, and bias, into a string
    @Override
    public String toString() {
        Map<Integer, Float> neuronString = synapses;
        neuronString.put(synapses.size(), getBias());
        return neuronString.toString() + "N";
    }
}