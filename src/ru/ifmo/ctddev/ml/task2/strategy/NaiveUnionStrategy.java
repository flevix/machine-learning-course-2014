package ru.ifmo.ctddev.ml.task2.strategy;

import java.util.StringTokenizer;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class NaiveUnionStrategy extends UnionSubjectBodyStrategy {

    @Override
    protected boolean validate(String data, boolean isSubject) {
        double spamProbability = getProbability(spamLetterCount, legitLetterCount);
        double legitProbability = getProbability(legitLetterCount, spamLetterCount);
        StringTokenizer st = new StringTokenizer(data, " ");
        if (isSubject) {
            st.nextToken();
        }
        while (st.hasMoreTokens()) {
            Long feature = Long.valueOf(st.nextToken());
            Long featureCountInSpam = spamLetterFeatures.getOrDefault(feature, 1L);
            Long featureCountInLegit = legitLetterFeatures.getOrDefault(feature, 1L);
            spamProbability *= getProbability(featureCountInSpam, featureCountInLegit);
            legitProbability *= getProbability(featureCountInLegit, featureCountInSpam);
        }
        return spamProbability > legitProbability;
    }

}
