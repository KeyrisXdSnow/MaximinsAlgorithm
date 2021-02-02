package sample.randomizer;

public class Random {

    public static int random(double min, double max) {
        return (int) (min + (int)(Math.random() * (max - min + 1)));
    }

}
