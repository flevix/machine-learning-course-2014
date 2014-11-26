package ru.ifmo.ctddev.ml.task5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nechaev Mikhail
 * Since 24/11/14.
 */
public class SVD {

    //Recommender Systems Handbook, p.152
    private final int f;
    private final Map<Long, double[]> pu; //user
    private final Map<Long, double[]> qi; //item
    private final Map<Long, Double> bu; //deviations of user u from the average
    private final Map<Long, Double> bi; //-//- item i
    private final double GAMMA = 0.005;
    private final double LAMBDA = 0.02;
    private final int ITERATION_COUNT = 1000;
    private double mu = 0D;

    SVD(int f) {
        this.f = f;
        this.qi = new HashMap<>();
        this.pu = new HashMap<>();
        this.bu = new HashMap<>();
        this.bi = new HashMap<>();
    }

    public void learn(List<Feature> features) {
        features.stream().forEach((feature) -> add(feature.getUser(), feature.getItem()));
        mu = features.stream().mapToDouble(Feature::getRating).average().getAsDouble();

        double prevRmse = 0;
        double rmse = 1;
        int iter = 0;
        while (ITERATION_COUNT > iter && Math.abs(rmse - prevRmse) > 0.00001) {
            iter++;
            prevRmse = rmse;
            rmse = 0;
            for (Feature feature : features) {
                Long item = feature.getItem(), user = feature.getUser(), rating = feature.getRating();
                Double cbu = bu.get(user), cbi = bi.get(item); //c -- current
                double[] cqi = qi.get(item), cpu = pu.get(user);

                double predictRating = mu + cbi + cbu + dotProduct(cqi, cpu);
                double error = rating - predictRating;
                rmse += error * error;
                bu.put(user, cbu + GAMMA * (error - LAMBDA * cbu));
                bi.put(item, cbi + GAMMA * (error - LAMBDA * cbi));
                for (int i = 0; i < f; i++) {
                    double qi = cqi[i], pu = cpu[i];
                    cqi[i] = qi + GAMMA * (error * pu - LAMBDA * qi);
                    cpu[i] = pu + GAMMA * (error * qi - LAMBDA * pu);
                }
            }
            rmse = Math.sqrt(rmse / features.size());
        }
    }

    private void add(long user, long item) {
        pu.putIfAbsent(user, new double[f]);
        qi.putIfAbsent(item, new double[f]);
        bu.putIfAbsent(user, 0.D);
        bi.putIfAbsent(item, 0.D);
    }

    private double dotProduct(double[] q_i, double[] p_u) {
        double res = 0;
        for (int i = 0; i < f; i++) {
            res += q_i[i] * p_u[i];
        }
        return res;
    }

    public double getRating(long user, long item) {
        add(user, item);
        return mu + bu.get(user) + bi.get(item) + dotProduct(pu.get(user), qi.get(item));
    }
}
