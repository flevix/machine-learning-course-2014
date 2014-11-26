package ru.ifmo.ctddev.ml.task5;

/**
 * Created by Nechaev Mikhail
 * Since 23/11/14.
 */
public class Feature {

    private long user;
    private long item;
    private long rating;

    Feature(long user, long item, long rating) {
        this.user = user;
        this.item = item;
        this.rating = rating;
    }

    public Long getUser() {
        return user;
    }

    public Long getItem() {
        return item;
    }

    public Long getRating() {
        return rating;
    }
}
