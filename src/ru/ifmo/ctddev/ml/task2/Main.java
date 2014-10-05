package ru.ifmo.ctddev.ml.task2;

import ru.ifmo.ctddev.ml.task2.strategy.*;
import ru.ifmo.ctddev.ml.utils.ClassificationTaskHelper;
import ru.ifmo.ctddev.ml.utils.file.CrossValidationFileSetManager;
import ru.ifmo.ctddev.ml.utils.file.FileSetProcessor;

import java.io.*;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class Main {

    private static final int FILE_COUNT = 10;
    private static final int TRAINING_SET_COUNT = 8;
    private static final String RESOURCES_PATH = "res/task2";
    private static final ClassificationTaskHelper classificationHelper = new ClassificationTaskHelper();

    private Strategy[] strategies = {
//            new DummyStrategy(),
            new NaiveUnionStrategy(),
            new NaiveSplitStrategy()
    };

    private void run(int trainingSetCount) {
        CrossValidationFileSetManager crossValidationFileSetManager;
        try {
            crossValidationFileSetManager = new CrossValidationFileSetManager(RESOURCES_PATH, FILE_COUNT, trainingSetCount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        FileSetProcessor trainingFileSetProcessor = crossValidationFileSetManager.getTrainingFileSetProcessor();
        FileSetProcessor validationFileSetProcessor = crossValidationFileSetManager.getValidationFileSetProcessor();
        Command learnCommand = new LearnCommand();
        Command validateCommand = new ValidateCommand();

        double bestF_measure = 0D;
        String bestStrategy = "";
        for (Strategy strategy : strategies) {
            learnCommand.setStrategy(strategy);
            validateCommand.setStrategy(strategy);

            process(trainingFileSetProcessor, learnCommand);
            process(validationFileSetProcessor, validateCommand);

            System.out.println(
                    "strategy = " + strategy.getClass().getSimpleName() + ", " +
                    "precision = " + classificationHelper.getPrecision() + ", " +
                    "recall = " + classificationHelper.getRecall() + ", " +
                    "F-measure = " + classificationHelper.getfMeasure()
            );

            if (bestF_measure < classificationHelper.getfMeasure()) {
                bestF_measure = classificationHelper.getfMeasure();
                bestStrategy = strategy.getClass().getSimpleName();
            }

            classificationHelper.reset();
            trainingFileSetProcessor.reset();
            validationFileSetProcessor.reset();
        }
        System.out.println("Best strategy is " + bestStrategy + ", where F-measure = " + bestF_measure);
    }

    private void process(FileSetProcessor processor, Command command) {
        while (processor.hasMoreElements()) {
            File curr = processor.nextElement();
            Letter letter = buildLetter(curr);
            if (letter != null) {
                command.execute(letter);
            }
        }
    }

    private Letter buildLetter(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            boolean isSpam = file.getName().contains("spmsg");
            String subject = reader.readLine();
            reader.readLine();
            String body = reader.readLine();
            return new Letter(isSpam, subject, body);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        new Main().run((args.length > 0) ? Integer.getInteger(args[0], TRAINING_SET_COUNT) : TRAINING_SET_COUNT);
    }

    private abstract class Command {
        protected Strategy strategy;
        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }
        public abstract void execute(Letter letter);
    }

    private class LearnCommand extends Command {
        @Override
        public void execute(Letter letter) {
            strategy.learn(letter);
        }
    }

     private class ValidateCommand extends Command {
        @Override
        public void execute(Letter letter) {
            boolean isSpam = strategy.validateSpam(letter);
            classificationHelper.update(letter.isSpam(), isSpam);
        }
    }

}
