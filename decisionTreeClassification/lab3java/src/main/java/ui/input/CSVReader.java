package ui.input;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class CSVReader {
    public static Dataset readCSVFileAsDataset(File CSVFile) {
        requireNonNull(CSVFile, "The given CSV file cannot be null!");
        if (!CSVFile.getName().endsWith(".csv")) throw new IllegalArgumentException("The given supposed CSV file is not a CSV file!");

        List<String> rows = new ArrayList<>();
        String row;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(CSVFile), StandardCharsets.UTF_8))) {
            while ((row = reader.readLine()) != null) rows.add(row);

            List<Example> examples = new ArrayList<>();
            String[] featureNames = rows.get(0).strip().split(",");
            int labelIndex = featureNames.length - 1;

            for (int i = 1, totalExamples = rows.size(); i < totalExamples; i++) {
                String[] exampleValues = rows.get(i).strip().split(",");
                Example currentExample = new Example(exampleValues[labelIndex]);
                for (int j = 0; j < labelIndex; j++) currentExample.addFeatureAndValue(featureNames[j], exampleValues[j]);
                examples.add(currentExample);
            }

            return new Dataset(examples);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve set information from the CSV file!", e);
        }
    }
}
