package sample.kmeans;


import sample.Constants;
import sample.Distance;
import sample.data.Cluster;
import sample.data.Point;


import java.util.List;

public class KMeansAlgorithm {

    public void start(List<Point> pointList, List<Cluster> clusters, int maxIterations) {

        List<Cluster> oldClustersState;

        for (int i = 0; i < maxIterations-1; i++) {

            oldClustersState = clusters;
            recalculateCenters(clusters);

            for (Point point : pointList) {
                definePointCluster(point, clusters);
            }

            if (i == maxIterations - 1) break;
            if (clusters.equals(oldClustersState)) break;

        }
    }

    public void generateFistState(List<Point> pointList, List<Cluster> clusters) {

        final double[] minX = {Constants.MAX_X};
        final double[] maxX = {Constants.MIN_X};
        final double[] minY = {Constants.MAX_Y};
        final double[] maxY = {Constants.MIN_Y};

        pointList.stream().forEach(point -> {
            if ( point.getX() < minX[0])
                minX[0] = point.getX();
            if (point.getX() > maxX[0])
                maxX[0] = point.getX();

            if ( point.getY() < minY[0])
                minY[0] = point.getX();
            if (point.getY() > maxY[0])
                maxY[0] = point.getY();

        });
        initClusters(clusters, minX[0], maxX[0], minY[0], maxY[0]);

        for (Point point : pointList) {
            definePointCluster(point, clusters);
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

    private void initClusters(List<Cluster> clusters) {

        for (int i = 0; i < clusters.size(); i++) {
            clusters.get(i).initCenters();
        }
    }

    private void initClusters(List<Cluster> clusters, double minX, double maxX, double minY,double maxY) {

        for (int i = 0; i < clusters.size(); i++) {
            clusters.get(i).initCenters(minX, maxX, minY, maxY);
        }
    }

    private void recalculateCenters(List<Cluster> clusters) {
        clusters.stream().forEach(cluster -> {
            List<Point> pointList = cluster.getPointList();

            if (pointList != null && !pointList.isEmpty()) {

                int sumX = 0, sumY = 0;

                for (Point point : pointList) {
                    sumX += point.getX();
                    sumY += point.getY();
                }

                int newX = sumX / pointList.size();
                int newY = sumY / pointList.size();

                cluster.setCenterX(newX);
                cluster.setCenterY(newY);

                pointList.clear();
            }
        });
    }

}
