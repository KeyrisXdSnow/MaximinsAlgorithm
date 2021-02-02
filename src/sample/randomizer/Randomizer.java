package sample.randomizer;

import static sample.Constants.*;

public class Randomizer implements IRandomizer {

    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;

    private static Randomizer randomizer;

    private Randomizer(int minX, int maxX, int minY, int maxY){
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public static Randomizer getInstance(int minX, int maxX, int minY, int maxY) {

        if (randomizer == null) {
            synchronized (Randomizer.class) {
                if (randomizer == null) {
                    randomizer = new Randomizer(minX,maxX,minY,maxY);
                }
            }
        }
        return randomizer;
    }

    public static Randomizer getInstance() {

        if (randomizer == null) {
            synchronized (Randomizer.class) {
                if (randomizer == null) {
                    randomizer = new Randomizer(MIN_X,MAX_X,MIN_Y,MAX_Y);
                }
            }
        }
        return randomizer;
    }

    @Override
    public int randomX() {
        return randomizer.minX + (int)(Math.random() * (randomizer.maxX - randomizer.minX + 1));
    }

    @Override
    public int randomY() {
        return randomizer.minY + (int)(Math.random() * (randomizer.maxY - randomizer.minY + 1));
    }

    @Override
    public double randomX(double minX, double maxX) {
        return minX + (int)(Math.random() * (maxX - minX + 1));
    }

    @Override
    public double randomY(double minY, double maxY) {
        return minY + (int)(Math.random() * (maxY - minY + 1));
    }

}


