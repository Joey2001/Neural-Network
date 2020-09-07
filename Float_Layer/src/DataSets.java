import java.util.ArrayList;

public class DataSets {

    private ArrayList<float[]> input;
    private ArrayList<float[]> target;

    public DataSets(){
        this.input = new ArrayList<>();
        this.target = new ArrayList<>();
    }

    public void addDataSet(float[] input, float[] target){
        this.input.add(input);
        this.target.add(target);
    }

    public float[][] getDataSet(int index){
        return new float[][]{this.input.get(index),this.target.get(index)};
    }

    public int size(){
        return this.input.size();
    }
}