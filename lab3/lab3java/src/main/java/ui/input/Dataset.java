package ui.input;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class Dataset {
    private final List<Example> examples;

    private final Set<String> featuresPresent;
    private Map<String, Integer> labelOccurrences;
    private double datasetEntropy = 0;
    private String mostFrequentLabel = null;
    private List<String> classLabelsPresent = null;
    private Map<String, Double> subtreeInformationGains = null;

    public Dataset(List<Example> examples) {
        requireNonNull(examples, "The given list of examples cannot be null!");

        this.examples = examples;
        featuresPresent = examples.get(0).getFeatures();
        calculateLabelOccurrences();
        calculateEntropy();
        getMostFrequentLabel();
        getDistinctLabels();
    }

    private void calculateLabelOccurrences() {
        labelOccurrences = examples.stream()
                .collect(Collectors.groupingBy(Example::getClassLabel)).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()));
    }

    private void calculateEntropy() {
        int numberOfExamples = examples.size();
        labelOccurrences.values().forEach(amount -> {
            double fraction = 1.0 * amount / numberOfExamples;
            datasetEntropy -= fraction * log2(fraction);
        });
    }

    private static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }

    public Map<String, Dataset> splitByFeature(String feature) {
        requireNonNull(feature, "The given feature cannot be null!");

        return examples.stream()
                .collect(Collectors.groupingBy(example -> example.getFeatureValue(feature))).entrySet().stream()
                .filter(entry -> entry.getKey().isPresent())
                .collect(Collectors.toMap(e -> e.getKey().get(), e -> new Dataset(e.getValue())));
    }

    public boolean containsExamples() {
        return !examples.isEmpty();
    }

    public String getMostFrequentLabel() {
        if (mostFrequentLabel == null) {
            int maxOccurrences = labelOccurrences.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElseThrow().getValue();

            mostFrequentLabel = labelOccurrences.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(maxOccurrences))
                    .map(Map.Entry::getKey).min(Comparator.naturalOrder()).orElseThrow();
        }

        return mostFrequentLabel;
    }

    public List<String> getDistinctLabels() {
        if (classLabelsPresent == null) classLabelsPresent = examples.stream()
                .map(Example::getClassLabel).distinct().collect(Collectors.toList());

        return classLabelsPresent;
    }

    public long getNumberOfDistinctLabels() {
        return classLabelsPresent.size();
    }

    public Map<String, Double> getInformationGains() {
        if (subtreeInformationGains == null) {
            Map<String, Map<String, Dataset>> newSubtrees = new HashMap<>();
            featuresPresent.forEach(feature -> newSubtrees.put(feature, splitByFeature(feature)));

            subtreeInformationGains = new HashMap<>();
            for (String feature : featuresPresent) {
                Map<String, Dataset> subtrees = newSubtrees.get(feature);
                double expectedEntropy = 0;

                for (Dataset value : subtrees.values())
                    expectedEntropy += value.datasetEntropy * (1.0 * value.examples.size() / examples.size());
                double informationGain = datasetEntropy - expectedEntropy;

                subtreeInformationGains.put(feature, informationGain);
            }
        }

        return subtreeInformationGains;
    }

    public Set<String> getFeaturesAvailable() {
        return featuresPresent;
    }

    public Iterator<Example> getExamplesIterator() {
        return examples.iterator();
    }
}

