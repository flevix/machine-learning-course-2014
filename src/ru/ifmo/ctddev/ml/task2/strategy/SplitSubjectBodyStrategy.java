package ru.ifmo.ctddev.ml.task2.strategy;

import ru.ifmo.ctddev.ml.task2.Letter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public abstract class SplitSubjectBodyStrategy extends Strategy {

    protected Map<Long, Long> spamLetterSubjectFeatures = new HashMap<>(1000);
    protected Map<Long, Long> spamLetterBodyFeatures = new HashMap<>(15000);
    protected Map<Long, Long> legitLetterSubjectFeatures = new HashMap<>(1000);
    protected Map<Long, Long> legitLetterBodyFeatures = new HashMap<>(15000);

    @Override
    public void learn(Letter letter) {
        updateLettersCount(letter.isSpam());
        process(letter.isSpam(), letter);
    }

    protected void process(boolean isSpam, Letter letter) {
        if (isSpam) {
            learn(spamLetterSubjectFeatures, letter.getSubject(), true);
            learn(spamLetterBodyFeatures, letter.getBody(), false);
        } else {
            learn(legitLetterSubjectFeatures, letter.getSubject(), true);
            learn(legitLetterBodyFeatures, letter.getBody(), false);
        }
    }
}
