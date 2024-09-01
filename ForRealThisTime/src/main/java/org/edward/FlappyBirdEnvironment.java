package org.edward;




public class FlappyBirdEnvironment {

    static Drawing drawing = new Drawing();
    public static int temp = -1;

    public FlappyBirdEnvironment() {

        drawing.run();
    }

    public double[] getData(){
       return normalize(drawing.getData(), -1000, 1000);
    }

    public double[] normalize(double[] data, double min, double max) {
        double[] normalizedData = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            normalizedData[i] = (data[i] - min) / (max - min);
        }
        return normalizedData;
    }


    public boolean isRunning(){
        return drawing.getTimer();
    }
    public void updateState(){
        if(!isRunning()){
            restart();
        }
    }
    public void restart(){
        drawing.restart();
        startTimer();
    }

    public void startTimer(){
        drawing.startTimer();
    }

    public int performAction(int actionVal){
        if(Drawing.generations % 50 == 0){
            if(temp != Drawing.generations){
                System.out.println("\rGenerations: " + Drawing.generations + "   Max Score: " + Drawing.maxScore);
                temp = Drawing.generations;
            }
        }
        if(actionVal >= 0.3) { // Lowered the jump threshold to encourage more jumps
            Player.jump(Drawing.jumpParts);
        }
        if(drawing.getTimer()){
            if(drawing.checkCollisions()){
                return -500; // Reduced collision penalty slightly
            } else {
                // Increased reward range, using a non-linear reward shaping
                double distanceReward = 500 - Math.pow(Math.abs(drawing.distanceBetweenPoint()), 2) / 100;
                return (int) Math.max(distanceReward / 150, -1); // Ensuring the reward is within a reasonable range
            }
        }
        return 1; // Small reward for surviving this frame
    }

}
