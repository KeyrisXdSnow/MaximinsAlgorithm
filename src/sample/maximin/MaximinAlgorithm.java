package sample.maximin;

import sample.Distance;
import sample.data.Cluster;
import sample.data.Point;
import sample.randomizer.Random;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MaximinAlgorithm {

    private List<Cluster> clusterList = new ArrayList<>();

    public List<Cluster> generateFistState(List<Point> pointList) {

        clusterList.clear();

        Cluster fistCluster = initFistCluster(pointList);
        clusterList.add(fistCluster);
        clusterList.add(initSecondCluster(pointList, clusterList.get(0).getCenterX(), clusterList.get(0).getCenterX()));

        definePointsCluster(pointList);

        return clusterList;
    }

    public List<Cluster> start(List<Point> pointList) {

        while (true) {

            Map<Point, Double> maxCenterDist = getMaxDistPoint();
            Map.Entry<Point, Double> entry = maxCenterDist.entrySet().stream()
                    .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();

            double dist = getArDistanceBetweenCenters(entry.getKey());
            if (entry.getValue() <= dist / 2)
                break;

            clusterList.add(new Cluster(entry.getKey().getX(), entry.getKey().getY()));
            definePointsCluster(pointList);

        }
        return clusterList;
    }

    private Cluster initFistCluster(List<Point> pointList) {
        clusterList = new ArrayList<>();
        int index = Random.random(0, pointList.size());

        Point centerPoint = pointList.get(index);

        Cluster cluster = new Cluster();
        cluster.setCenterX(centerPoint.getX());
        cluster.setCenterY(centerPoint.getY());

        return cluster;
        // возможно нужно удалить точку из pointList
    }

    private Cluster initSecondCluster(List<Point> pointList, double centerX, double centerY) {
        Point point = findSecondPoint(pointList, centerX, centerY);

        Cluster cluster = new Cluster();
        cluster.setCenterX(point.getX());
        cluster.setCenterY(point.getY());

        // возможно нужно удалить точку из pointList
        return cluster;
    }

    private Point findSecondPoint(List<Point> pointList, double centerX, double centerY) {
        final Point[] maxDistPoint = new Point[1];
        final double[] maxDistance = {-1};
        pointList.stream().forEach(point -> {
            double dist = Distance.calculateEuclideanDistance(point.getX(), point.getY(), centerX, centerY);
            if (dist > maxDistance[0]) {
                maxDistance[0] = dist;
                maxDistPoint[0] = point;
            }
        });
        return maxDistPoint[0];
    }

    private void definePointsCluster(List<Point> pointList) {

        clusterList.stream().forEach(cluster -> cluster.getPointList().clear());
        for (Point point : pointList) {
            definePointCluster(point, clusterList);
        }
    }

    private void definePointCluster(Point point, List<Cluster> clusters) {

        Cluster neededCluster = null;
        double minDist = -1;

        for (Cluster cluster : clusters) {
            double dist = Distance.calculateEuclideanDistance(cluster.getCenterX(), cluster.getCenterY(), point.getX(), point.getY());
            if (dist < minDist || minDist == -1) {
                minDist = dist;
                neededCluster = cluster;
            }
        }
        try {
            neededCluster.addPoint(point);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Map<Point, Double> getMaxDistPoint() {

        Map<Point, Double> maxPointMap = new HashMap<>();

        clusterList.stream().forEach(cluster -> {

            AtomicReference<Double> maxDistance = new AtomicReference<>(0.0);
            AtomicReference<Point> maxDistPoint = new AtomicReference<>();

            cluster.getPointList().forEach(point -> {
                double dist = Distance.calculateEuclideanDistance(cluster.getCenterX(), cluster.getCenterY(), point.getX(), point.getY());
                if (dist > maxDistance.get()) {
                    maxDistance.set(dist);
                    maxDistPoint.set(point);
                }
            });
            maxPointMap.put(maxDistPoint.get(), maxDistance.get());
        });
        return maxPointMap;
    }

    private Double getArDistanceBetweenCenters(Point point) {

        AtomicReference<Double> aSumX = new AtomicReference<>(0.0);
        AtomicReference<Double> aSumY = new AtomicReference<>(0.0);

        clusterList.stream().forEach(cluster -> {
            aSumX.updateAndGet(v -> new Double((double) (v + cluster.getCenterX())));
            aSumY.updateAndGet(v -> new Double((double) (v + cluster.getCenterY())));
        });

        double sumX = aSumX.get() / clusterList.size();
        double sumY = aSumY.get() / clusterList.size();

        return Distance.calculateEuclideanDistance(sumX, sumY, point.getX(), point.getY());
    }

}
