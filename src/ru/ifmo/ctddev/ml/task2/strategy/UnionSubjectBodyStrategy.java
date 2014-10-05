package ru.ifmo.ctddev.ml.task2.strategy;

import ru.ifmo.ctddev.ml.task2.Letter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public abstract class UnionSubjectBodyStrategy extends Strategy {

    protected Map<Long, Long> spamLetterFeatures = new HashMap<>(15000);
    protected Map<Long, Long> legitLetterFeatures = new HashMap<>(15000);

    @Override
    public void learn(Letter letter) {
        updateLettersCount(letter.isSpam());
        process(letter.isSpam() ? spamLetterFeatures : legitLetterFeatures, letter);
    }

    protected void process(Map<Long, Long> store, Letter letter) {
        learn(store, letter.getSubject(), true);
        learn(store, letter.getBody(), false);
    }
}
