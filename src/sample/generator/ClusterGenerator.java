package sample.generator;

import sample.data.Cluster;

import java.util.ArrayList;
import java.util.List;

public class ClusterGenerator implements IGenerator {

    @Override
    public List<Cluster> generate(int amount) {
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            clusters.add(new Cluster());
        }
        return clusters;
    }
}
