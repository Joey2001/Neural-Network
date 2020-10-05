import java.io.*;
import java.util.ArrayList;

public class ReconnectBrain {

    private String location;

    public ReconnectBrain(String location){
        this.location = location;
    }

//    public interface to get the layer array
    public Layer[] reconstruct(){
        ArrayList<String[]> rawData = pullFromText(location);
        return rawData != null ? reformat(rawData) : null;
    }

//    takes the raw list of strings, parses the data, and returns an array of layers
    private Layer[] reformat(ArrayList<String[]> preProcess){
        Layer[] reconstruction = new Layer[preProcess.size()];

        for(int layer = 0; layer < reconstruction.length; layer++){
            Neuron[] neurons = new Neuron[preProcess.get(layer).length];

            for(int neuron = 0; neuron < neurons.length; neuron++){
                int trim = preProcess.get(layer)[neuron].length() - 1;

                preProcess.get(layer)[neuron] = preProcess.get(layer)[neuron].substring(1, trim).replaceAll("\\s+", "");
                String[] synapses = preProcess.get(layer)[neuron].split(",");
                neurons[neuron] = new Neuron();

                if(layer == reconstruction.length - 1)
                    neurons[neuron].setActivation(activation.LEAKY_REC_LIN);

                for (int synapse = 0; synapse < synapses.length; synapse++) {
                    String[] connection = synapses[synapse].split("=");

                    if (!synapses[synapse].contains("null")) {
                        int location = Integer.parseInt(connection[0]);
                        float weight = Float.parseFloat(connection[1]);

                        if (synapse < synapses.length - 1)
                            neurons[neuron].setSynapse(location, weight);
                        else
                            neurons[neuron].setBias(weight);
                    }
                }
            }
            reconstruction[layer] = new Layer(neurons);
        }
        return reconstruction;
    }

//    reads from the text file to create a list of strings to be parsed
    private ArrayList<String[]> pullFromText(String finalDestination) {
        try(FileReader fileReader = new FileReader(finalDestination)){
            int i;
            String brainString = "";

            while((i = fileReader.read()) != -1)
                brainString += (char) i;

            String[] splitLayer = brainString.split("L");
            ArrayList<String[]> splitNeuron = new ArrayList<>();

            for (String string : splitLayer)
                splitNeuron.add(string.split("N"));

            return splitNeuron;
        }catch(FileNotFoundException ignored){
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}