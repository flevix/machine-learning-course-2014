package ru.ifmo.ctddev.ml.task2.strategy;

import ru.ifmo.ctddev.ml.task2.Letter;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public abstract class Strategy {

    protected long spamLetterCount = 0L;
    protected long legitLetterCount = 0L;

    public abstract void learn(Letter letter);

    protected void learn(Map<Long, Long> store, String data, boolean isSubject) {
        StringTokenizer st = new StringTokenizer(data, " ");
        if (isSubject) {
            st.nextToken();
        }
        while (st.hasMoreTokens()) {
            Long feature = Long.valueOf(st.nextToken());
            store.put(feature, store.getOrDefault(feature, 0L) + 1);
        }
    }

    public boolean validateSpam(Letter letter) {
        return validate(letter.getSubject(), true) || validate(letter.getBody(), false);
    }

    protected abstract boolean validate(String data, boolean isSubject);

    protected void updateLettersCount(boolean isSpam) {
        if (isSpam) {
            spamLetterCount++;
        } else {
            legitLetterCount++;
        }
    }

    protected double getProbability(Long x, Long y) {
        return x * 1D / (x + y);
    }

}
