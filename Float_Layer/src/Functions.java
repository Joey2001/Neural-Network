enum activation{SIGMOID, REC_LIN, LEAKY_REC_LIN}
public class Functions {
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

    private float sigmoid(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    private float sigmoidPrime(float x){
        return sigmoid(x) * (1 - sigmoid(x));
    }

    public float getRandomFloat(float[] bounds){
        return (float) (bounds[0] + Math.random() * (bounds[1] - bounds[0]));
    }
}
