package org.edward;

public class ReinforcementLearning {
    private final NeuralNetwork nn;
    private final EpsilonGreedy policy;
    private double gamma = 0.99; // Discount factor for future rewards

    public ReinforcementLearning() {
        // Initialize neural network with 3 inputs (state) and 1 output (Q-values for each action)
        nn = new NeuralNetwork(new LeakyReLUActivation(0.01), new MeanSquaredErrorLoss(), new SGDOptimizer(0.005));
        nn.addLayer(16, 2); // First hidden layer with 16 neurons and 3 inputs
        nn.addLayer(16, 16); // Second hidden layer with 16 neurons
        nn.addLayer(8, 16); // Third hidden layer with 8 neurons
        nn.addLayer(1, 8); // Output layer with 1 neuron (Q-value) and 8 inputs from the previous layer

        policy = new EpsilonGreedy(0.5, 0.9999, 0.005); // Epsilon decay over time
    }

    public void trainInRealTime(FlappyBirdEnvironment game) {
        while (game.isRunning()) {

            double[] state = game.getData();
            double[] qValues = nn.predict(state);

            // Choose an action based on the epsilon-greedy policy
            int action = policy.selectAction(qValues);

            // Take action in the game environment
            double reward = game.performAction(action);

            // Get new state after action
            double[] nextState = game.getData();
            double[] nextQValues = nn.predict(nextState);

            // Update Q-values using the reward and future reward estimation
            qValues[0] = reward + gamma * getMaxValue(nextQValues);
           // System.out.print("\r" + nn.predict(nextState)[0]);


            // Train the neural network with the updated Q-values
            nn.train(new double[][]{state}, new double[][]{qValues}, 1);


            System.out.print("\r  Action: " + action + "Epsilon: " + policy.getEpsilon() + "  Reward: " + reward + "  qValue: " + qValues[0]);
            // Decay epsilon after each action
            policy.decayEpsilon();

            // If game over, reset game and potentially reduce epsilon further
            game.updateState();

            try {
                Thread.sleep(50); // Sleep for 10 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private double getMaxValue(double[] values) {
        double max = values[0];
        for (double value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        ReinforcementLearning flappyBirdRL = new ReinforcementLearning();
        FlappyBirdEnvironment game = new FlappyBirdEnvironment(); // Assume this is your Flappy Bird game class
        flappyBirdRL.trainInRealTime(game);
    }
}

