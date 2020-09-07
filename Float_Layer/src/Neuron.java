import java.util.*;

public class Neuron {
    private float[] attributes;
    private Map<Integer, Float> synapses;
    private activation function;

    Neuron(){
        attributes = new float[3];
        synapses = new HashMap<>();
        function = activation.SIGMOID;
    }

    public float getData(){
        return attributes[0];
    }

    public void setData(float data){
        attributes[0] = data;
    }

    public float getError(){
        return attributes[1];
    }

    public void setError(float error){
        attributes[1] = error;
    }

    public float getBias(){
        return attributes[2];
    }

    public void setBias(float bias){
        attributes[2] = bias;
    }

    public float getSynapse(int location){
        return synapses.get(location);
    }

    public void setSynapse(int location, float strength){
        synapses.remove(location);
        synapses.put(location, strength);
    }

    public activation getActivation(){
        return function;
    }

    public void setActivation(activation function){
        this.function = function;
    }

    public float largestSynapse() {
        float bigNumber = 0f;
        for(int number : synapses.keySet())
            bigNumber = Math.max(synapses.get(number), bigNumber);
        return bigNumber;
    }

    @Override
    public String toString() {
        Map<Integer, Float> neuronString = synapses;
        neuronString.put(synapses.size(), getBias());
        return neuronString.toString() + "N";
    }

    public void pruneSynapses(float threshold){
        for(int key = 0; key < synapses.size(); key++)
            if(Math.abs(synapses.get(key)) < threshold)
                setSynapse(key, 0f);
    }
}