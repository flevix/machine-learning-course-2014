package ru.ifmo.ctddev.ml.utils;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class ClassificationTaskHelper {

    private static final double eps = 1e-3;
    private double tp = 0L;
    private double tn = 0L;
    private double fp = 0L;
    private double fn = 0L;

    private double precision = 0D;
    private double recall = 0D;
    private double fMeasure = 0D;

    public void update(boolean trulyValue, boolean hypothesis) {
        if (trulyValue && hypothesis) {
            tp++;
        } else if (trulyValue) {
            fn++;
        } else if (hypothesis) {
            fp++;
        } else {
            tn++;
        }
        precision = safeDivision(tp, tp + fp);
        recall = safeDivision(tp, tp + fn);
        fMeasure = safeDivision(2 * precision * recall, precision + recall);
    }

    public void reset() {
        tp = fn = fp = tn = 0D;
        precision = recall = fMeasure = 0D;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    private double safeDivision(double divident, double divisor) {
        return isEqual(divisor, 0D) ? 0D : divident / divisor;
    }

    private boolean isEqual(double x, double y) {
        return Math.abs(x - y) < eps;
    }
}
