package ru.ifmo.ctddev.ml.task5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Nechaev Mikhail
 * Since 24/11/14.
 */
public class SVD {

    //Recommender Systems Handbook, p.152
    private final int f;
    private final Random random = new Random();
    private final Map<Long, double[]> pu; //user
    private final Map<Long, double[]> qi; //item
    private final Map<Long, Double> bu; //deviations of user u from the average
    private final Map<Long, Double> bi; //-//- item i
    private final double GAMMA = 0.005D;
    private final double LAMBDA = 0.02D;
    private final int ITERATION_COUNT = 100;
    private double mu = 0.D;


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

        double prevRmse = 0.D;
        double rmse = 1.D;
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
        pu.putIfAbsent(user, getRandomArray());
        qi.putIfAbsent(item, getRandomArray());
        bu.putIfAbsent(user, 0.D);
        bi.putIfAbsent(item, 0.D);
    }

    private double[] getRandomArray() {
        double[] a = new double[f];
        for (int i = 0; i < f; i++) {
            a[i] = random.nextDouble();
        }
        return a;
    }

    private double dotProduct(double[] q_i, double[] p_u) {
        double res = 0;
        for (int i = 0; i < f; i++) {
            res += q_i[i] * p_u[i];
        }
        return res;
    }

    public double getRating(long user, long item) {
        return mu + bu.getOrDefault(user, 0.D) + bi.getOrDefault(item, 0.D)
                + dotProduct(pu.getOrDefault(user, new double[f]), qi.getOrDefault(item, new double[f]));
    }
}
