package ru.ifmo.ctddev.ml.utils.file;

import java.io.File;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Created by Nechaev Mikhail
 * Since 05/10/14.
 */
public class FileSetProcessor implements Enumeration<File> {

    File[][] files;
    int currentDir;
    int currentFileInDir;

    FileSetProcessor(File[] dirs) {
        files = new File[dirs.length][];
        int c = 0;
        for (File dir : dirs) {
            files[c++] = dir.listFiles();
        }
        this.currentDir = 0;
        this.currentFileInDir = 0;
    }

    @Override
    public boolean hasMoreElements() {
        return currentDir < files.length && currentFileInDir < files[currentDir].length;
    }

    @Override
    public File nextElement() {
        if (currentDir >= files.length) {
            throw new NoSuchElementException();
        }
        File file = getCurrentFile();
        currentFileInDir++;
        if (files[currentDir].length <= currentFileInDir) {
            currentDir++;
            currentFileInDir = 0;
        }
        return file;
    }

    private File getCurrentFile() {
        return files[currentDir][currentFileInDir];
    }
}
