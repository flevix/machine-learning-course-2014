package ru.ifmo.ctddev.ml.task2.strategy;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class NaiveSplitStrategy extends SplitSubjectBodyStrategy {

    @Override
    protected boolean validate(String data, boolean isSubject) {
        double spamProbability = getProbability(spamLetterCount, legitLetterCount);
        double legitProbability = getProbability(legitLetterCount, spamLetterCount);
        StringTokenizer st = new StringTokenizer(data, " ");
        Map<Long, Long> spamLetterFeatures;
        Map<Long, Long> legitLetterFeatures;
        if (isSubject) {
            st.nextToken();
            spamLetterFeatures = spamLetterSubjectFeatures;
            legitLetterFeatures = legitLetterSubjectFeatures;
        } else {
            spamLetterFeatures = spamLetterBodyFeatures;
            legitLetterFeatures = legitLetterBodyFeatures;
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
