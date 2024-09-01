package org.edward;

public class MeanSquaredErrorLoss implements LossFunction {
    @Override
    public double computeError(double target, double output) {
        return 0.5 * Math.pow(target - output, 2); // 0.5 factor simplifies derivative
    }

    @Override
    public double derivative(double target, double output) {
        return output - target; // Derivative of MSE with respect to output
    }
}
