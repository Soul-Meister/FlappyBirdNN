package org.edward;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private List<Layer> layers;
    private ActivationFunction activationFunction;
    private LossFunction lossFunction;
    private Optimizer optimizer;

    public NeuralNetwork(ActivationFunction activationFunction, LossFunction lossFunction, Optimizer optimizer) {
        this.layers = new ArrayList<>();
        this.activationFunction = activationFunction;
        this.lossFunction = lossFunction;
        this.optimizer = optimizer;
    }

    public void addLayer(int numNeurons, int numInputsPerNeuron) {
        Layer layer = new Layer(numNeurons, numInputsPerNeuron);
        layers.add(layer);
    }

    public double[] predict(double[] inputs) {
        double[] outputs = inputs;
        for (Layer layer : layers) {
            outputs = layer.feedForward(outputs, activationFunction);
        }
        return outputs;
    }

    public void train(double[][] inputs, double[][] targets, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                double[] output = predict(inputs[i]);
                backpropagate(targets[i]); // Ensure targets[i] matches output size
            }
        }
    }


    public void backpropagate(double[] target) {
        // Assuming you have a configurable learning rate
        double learningRate = 0.00001; // or get this from your optimizer

        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            double[] errors = new double[layer.getNeurons().length];

            if (i == layers.size() - 1) { // Output layer
                for (int j = 0; j < layer.getNeurons().length; j++) {
                    Neuron neuron = layer.getNeurons()[j];
                    errors[j] = target[j] - neuron.getOutput(); // Error for each output neuron
                    neuron.setDelta(errors[j] * activationFunction.derivative(neuron.getOutput()));
                    //System.out.println("Layer " + i + " Neuron " + j + " Delta: " + neuron.getDelta());
                }
            } else { // Hidden layers
                Layer nextLayer = layers.get(i + 1);
                for (int j = 0; j < layer.getNeurons().length; j++) {
                    double error = 0.0;
                    for (int k = 0; k < nextLayer.getNeurons().length; k++) {
                        Neuron nextNeuron = nextLayer.getNeurons()[k];
                        error += nextNeuron.getWeights()[j] * nextNeuron.getDelta();
                    }
                    Neuron neuron = layer.getNeurons()[j];
                    neuron.setDelta(error * activationFunction.derivative(neuron.getOutput()));
                    // Print the delta (gradient) for the hidden neuron
                    //System.out.println("Layer " + i + " Neuron " + j + " Delta: " + neuron.getDelta());
                }
            }

            // Weight and bias update
            for (Neuron neuron : layer.getNeurons()) {
                for (int j = 0; j < neuron.getWeights().length; j++) {
                    double weightUpdate = learningRate * neuron.getDelta() * neuron.getInput(j);
                    neuron.getWeights()[j] -= weightUpdate;
                    // Print the weight gradient (which is delta * input)
                   // System.out.println("Layer " + i + " Neuron " + j + " Weight " + j + " Gradient: " + weightUpdate);
                }
                double biasUpdate = learningRate * neuron.getDelta();
                neuron.setBias(neuron.getBias() - biasUpdate);
                // Print the bias gradient (which is just the delta)
               // System.out.println("Layer " + i + "Bias Gradient: " + biasUpdate);
            }
        }
    }




}
