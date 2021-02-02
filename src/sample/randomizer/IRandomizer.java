package sample.randomizer;

public interface IRandomizer {
    int randomX();
    int randomY();

    double randomX(double minX, double maxX);

    double randomY(double minY, double maxY);
}
