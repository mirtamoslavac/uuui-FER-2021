package ui.input;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class Example {
    private final Map<String, String> featuresAndValues;
    private final String classLabel;

    public Example(String classLabel) {
        this.classLabel = requireNonNull(classLabel, "The given class label cannot be null!");
        featuresAndValues = new HashMap<>();
    }

    public String getClassLabel() {
        return this.classLabel;
    }

    public void addFeatureAndValue(String feature, String value) {
        requireNonNull(feature, "The given feature cannot be null!");
        requireNonNull(value, "The given value cannot be null!");

        featuresAndValues.put(feature, value);
    }

    public Optional<String> getFeatureValue(String feature) {
        requireNonNull(feature, "The given feature cannot be null!");

        String value = featuresAndValues.get(feature);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    public Set<String> getFeatures() {
        return featuresAndValues.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        featuresAndValues.forEach((key, value) -> sb.append(key).append("=").append(value).append(", "));
        return sb + classLabel;
    }
}
