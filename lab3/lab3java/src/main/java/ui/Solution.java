package ui;

import ui.algorithms.*;
import ui.input.*;

import java.io.File;

import static java.util.Objects.requireNonNull;

public class Solution {

    public static void main(String... args) {
        try {
            if (args.length < 2)
                throw new IllegalArgumentException("Not enough arguments given! Expected at least 2, got " + args.length + "!");
            if (args.length > 3)
                throw new IllegalArgumentException("Too many arguments given! Expected more than 3, got " + args.length + "!");

            File trainingSetFile, testSetFile;
            Integer maximumDepthHyperparameter = null;


            trainingSetFile = new File(args[0]).getAbsoluteFile();
            checkFile(trainingSetFile, "the training set");

            testSetFile = new File(args[1]).getAbsoluteFile();
            checkFile(testSetFile, "the test set");

            if (args.length == 3) maximumDepthHyperparameter = Integer.parseInt(args[2]);

            MLAlgorithm model;
            if (maximumDepthHyperparameter == null) model = new ID3Algorithm();
            else model = new ID3Algorithm(maximumDepthHyperparameter);

            Dataset trainingSet = CSVReader.readCSVFileAsDataset(trainingSetFile);
            Dataset testSet = CSVReader.readCSVFileAsDataset(testSetFile);

            model.fit(trainingSet);
            model.predict(testSet);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void checkFile(File file, String output) {
        requireNonNull(file, "The given file cannot be null");
        requireNonNull(output, "The given string to be included in the output cannot be null");

        if (!file.exists())
            throw new IllegalArgumentException("The given file containing " + output + " does not exist!");
        if (!file.canRead())
            throw new IllegalArgumentException("The given file containing " + output + " cannot be read!");
    }
}
