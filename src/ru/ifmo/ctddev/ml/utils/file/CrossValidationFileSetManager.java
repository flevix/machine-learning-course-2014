package ru.ifmo.ctddev.ml.utils.file;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class CrossValidationFileSetManager {

    private File[] listFiles;
    private FileSetProcessor trainingFileSetProcessor;
    private FileSetProcessor validationFileSetProcessor;

    public CrossValidationFileSetManager(String pathname, int filesCount, int trainingSetCount) throws FileNotFoundException {
        File root = new File(pathname);
        this.listFiles = root.listFiles();
        if (!root.isDirectory()) {
            throw new FileNotFoundException("File " + pathname + " is not a directory");
        }
        if (this.listFiles == null) {
            throw new FileNotFoundException("No files in " + pathname);
        }
        if (this.listFiles.length != filesCount) {
            throw new FileNotFoundException("No " + filesCount + " files in this directory. " +
                                            "Files count equal " + this.listFiles.length);
        }
        {
            boolean existNotDirectoryFile = false;
            String badFileName = "";
            for (File file : this.listFiles) {
                if (!file.isDirectory()) {
                    existNotDirectoryFile = true;
                    badFileName = file.getName();
                }
            }
            if (existNotDirectoryFile) {
                throw new FileNotFoundException("File " + badFileName + " is not a directory");
            }
        }
        if (trainingSetCount < 1 || trainingSetCount >= filesCount) {
            throw new IllegalArgumentException("Training file count must be " + ((trainingSetCount < 1)
                                                                               ? "positive"
                                                                               : "less then files counts"));
        }
        this.trainingFileSetProcessor = new FileSetProcessor(getFileSet(0, trainingSetCount));
        this.validationFileSetProcessor = new FileSetProcessor(getFileSet(trainingSetCount, filesCount));
    }

    public FileSetProcessor getTrainingFileSetProcessor() {
        return trainingFileSetProcessor;
    }

    public FileSetProcessor getValidationFileSetProcessor() {
        return validationFileSetProcessor;
    }

    private File[] getFileSet(int from, int to) {
        File[] files = new File[to - from];
        for (int i = from, curr = 0; i < to; i++, curr++) {
            files[curr] = listFiles[i];
        }
        return files;
    }

}
