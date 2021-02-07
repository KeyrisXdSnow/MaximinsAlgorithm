package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.kmeans.KMeansAlgorithm;
import sample.maximin.MaximinAlgorithm;
import sample.data.Cluster;
import sample.data.Point;
import sample.generator.IGenerator;
import sample.generator.PointGenerator;


import java.util.ArrayList;
import java.util.List;

public class Controller {

    private IGenerator pointGenerator;
    private MaximinAlgorithm maximinAlgorithm;
    private KMeansAlgorithm kMeansAlgorithm;
    private List<Point> pointList = new ArrayList<>();
    private List<Cluster> clusterList = new ArrayList<>();

    @FXML
    private BubbleChart<?, ?> beforeBubbleChart, afterBubbleChart;
    @FXML
    private Button bKmeans;
    @FXML
    private TextField tfNumberOfPoints;

    @FXML
    void initialize() {

        pointGenerator = new PointGenerator();
        maximinAlgorithm = new MaximinAlgorithm();
        kMeansAlgorithm = new KMeansAlgorithm();

        bKmeans.setOnAction(event -> {
            generatePoints();
            calculateKMeans();

        });
    }

    private void generatePoints() {
        pointList.clear();
        try {
            int pointsAmount = Integer.parseInt(tfNumberOfPoints.getText());
            pointList = pointGenerator.generate(pointsAmount);
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }

    }

    private void viewPointsAfterBubbleChart (BubbleChart bubbleChart) {
        bubbleChart.getData().clear();

        for (Cluster cluster : clusterList) {
            XYChart.Series series = new XYChart.Series();

            for (Point point : cluster.getPointList()) {
                series.getData().add(new XYChart.Data(point.getX(),point.getY(),Constants.POINT_SCALE));
            }
            bubbleChart.getData().add(series);
        }

        XYChart.Series series = new XYChart.Series();
        for (Cluster cluster : clusterList) {

            series.getData().add(new XYChart.Data(cluster.getCenterX(),cluster.getCenterY(),Constants.POINT_SCALE+5));

        }
        bubbleChart.getData().add(series);


    }

    private void calculateKMeans () {
        System.out.println("Fist iteration...");
        clusterList = maximinAlgorithm.generateFistState(pointList);
        System.out.println("Finish work");
        clusterList = maximinAlgorithm.start(pointList);
        viewPointsAfterBubbleChart(beforeBubbleChart);
        System.out.println("K-means algorithm working");
        kMeansAlgorithm.start(pointList,clusterList,Constants.MAX_ITERATIONS);
        viewPointsAfterBubbleChart(afterBubbleChart);


    }

}
