enum activation{SIGMOID, REC_LIN, LEAKY_REC_LIN}
public class Functions {
//    takes in a value, activation function, and whether or not its the function's derivative
//    to determine the value of the normalized value
    public float getValue(activation function, float x, boolean derivative){
        switch (function){
            case SIGMOID:
                return derivative ? this.sigmoidPrime(x) : this.sigmoid(x);
            case REC_LIN:
                return derivative ? (x > 0 ? 1 : 0) : (x > 0 ? x : 0);
            case LEAKY_REC_LIN:
                return derivative ? (x > 0 ? 1 : 0.01f) : (x > 0 ? x : .01f * x);
        }
        return -1f;
    }

//    This is the sigmoid function: Domain (-Infinity, Infinity) Range(0, 1)
//    This function maps all values to be between 0 and 1
    private float sigmoid(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

//    This returns the slope of the sigmoid function at a particular value
    private float sigmoidPrime(float x){
        return sigmoid(x) * (1 - sigmoid(x));
    }

//    returns a random number between the numbers specified by the bounds
    public float getRandomFloat(float[] bounds){
        return (float) (bounds[0] + Math.random() * (bounds[1] - bounds[0]));
    }
}