package org.edward;

import java.util.Random;

public class EpsilonGreedy {
    private double epsilon;
    private double decayRate;
    private double minEpsilon;
    private Random random;

    public EpsilonGreedy(double initialEpsilon, double decayRate, double minEpsilon) {
        this.epsilon = initialEpsilon;
        this.decayRate = decayRate;
        this.minEpsilon = minEpsilon;
        this.random = new Random();
    }

    public int selectAction(double[] qValues) {
        if (random.nextDouble() < epsilon) {
            return (int) Math.floor(Math.random() * (qValues.length + 1)); // Explore: random action
        } else {
            return getMaxIndex(qValues); // Exploit: best known action
        }
    }

    private int getMaxIndex(double[] values) {
        int maxIndex = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] > values[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public void decayEpsilon() {
        if (epsilon > minEpsilon) {
            epsilon *= decayRate;
            if (epsilon < minEpsilon) {
                epsilon = minEpsilon;
            }
        }
    }

    public double getEpsilon() {
        return epsilon;
    }
}
