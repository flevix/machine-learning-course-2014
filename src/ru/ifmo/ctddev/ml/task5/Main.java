package ru.ifmo.ctddev.ml.task5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nechaev Mikhail
 * Since 23/11/14.
 */
public class Main {

    private static final String RESOURCES_PATH = "res/task5/";
    private static final String TEST_IDS_FILENAME = RESOURCES_PATH + "test-ids.csv";
    private static final String OUTPUT = RESOURCES_PATH + "output.csv";
    private static final String[] TRAIN_FILESET = {RESOURCES_PATH + "train.csv", RESOURCES_PATH + "validation.csv"};

    private List<Feature> features = new ArrayList<>();

    private void run() throws IOException {
        for (String filename : TRAIN_FILESET) {
            File file = new File(filename);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                long[] items = getItems(line);
                features.add(new Feature(items[0], items[1], items[2]));
            }
        }
        SVD svd = new SVD(4);
        svd.learn(features);
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(TEST_IDS_FILENAME)));
            PrintWriter printWriter = new PrintWriter(new File(OUTPUT));
            printWriter.println("id,rating");
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                long[] items = getItems(line);
                printWriter.println(items[0] + "," + svd.getRating(items[1], items[2]));
            }
            printWriter.flush();
            printWriter.close();
        }
    }

    private long[] getItems(String data) {
        String[] dx = data.split(",");
        long[] items = new long[dx.length];
        for (int i = 0; i < dx.length; i++) {
            items[i] = Long.valueOf(dx[i]);
        }
        return items;
    }

    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
