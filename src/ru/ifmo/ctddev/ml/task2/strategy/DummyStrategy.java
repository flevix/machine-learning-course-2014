package ru.ifmo.ctddev.ml.task2.strategy;

import ru.ifmo.ctddev.ml.task2.Letter;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class DummyStrategy extends Strategy {
    @Override
    public void learn(Letter letter) {
        /* empty */
    }

    @Override
    public boolean validateSpam(Letter letter) {
        return letter.isSpam();
    }

    @Override
    protected boolean validate(String data, boolean isSubject) {
        return true;
    }
}
