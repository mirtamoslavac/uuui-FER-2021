package ui.algorithms;

import ui.input.Dataset;

public interface MLAlgorithm {
    void fit(Dataset trainingSet);
    void predict(Dataset testSet);
}
