package ru.ifmo.ctddev.ml.task2;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class Letter {

    private boolean isSpam;
    private String subject;
    private String body;

    public Letter(boolean isSpam, String subject, String body) {
        this.isSpam = isSpam;
        this.subject = subject;
        this.body = body;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
