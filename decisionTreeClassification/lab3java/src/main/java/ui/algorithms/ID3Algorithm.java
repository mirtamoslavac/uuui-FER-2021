package ui.algorithms;

import ui.input.*;
import ui.exceptions.DecisionTreeException;
import ui.nodes.*;
import ui.utils.OutputUtils;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class ID3Algorithm implements MLAlgorithm {
    private TreeNode root;
    private final Double maximumDepthHyperparameter;

    public ID3Algorithm() {
        this.maximumDepthHyperparameter = Double.POSITIVE_INFINITY;
    }

    public ID3Algorithm(int maximumDepthHyperparameter) {
        this.maximumDepthHyperparameter = maximumDepthHyperparameter * 1.0;
    }

    @Override
    public void fit(Dataset trainingSet) {
        requireNonNull(trainingSet, "The given training set cannot be null!");

        root = generateDecisionTree(trainingSet, trainingSet, trainingSet.getFeaturesAvailable(), 0);
        OutputUtils.printBranches(root);
    }

    @Override
    public void predict(Dataset testSet) {
        requireNonNull(testSet, "The given test set cannot be null!");

        List<String> predictions = new ArrayList<>();
        Iterator<Example> it = testSet.getExamplesIterator();
        it.forEachRemaining(example -> predictions.add(getPrediction(example, root)));

        OutputUtils.printPredictions(predictions);
        OutputUtils.printAccuracy(calculateAccuracy(predictions, testSet));
        OutputUtils.printConfusionMatrix(getConfusionMatrix(predictions, testSet), testSet.getDistinctLabels());
    }

    private TreeNode generateDecisionTree(Dataset set, Dataset parentSet, Set<String> featuresAvailable, int depth) {
        requireNonNull(set, "The given set cannot be null!");
        requireNonNull(parentSet, "The given parent set cannot be null!");
        requireNonNull(featuresAvailable, "The given list of available features cannot be null!");

        if (!maximumDepthHyperparameter.isInfinite() && depth >= maximumDepthHyperparameter.intValue())
            return new Leaf(set.getMostFrequentLabel(), depth);

        if (!set.containsExamples()) return new Leaf(parentSet.getMostFrequentLabel(), depth);

        String mostFrequentLabel = set.getMostFrequentLabel();
        if (featuresAvailable.isEmpty() || set.getNumberOfDistinctLabels() == 1)
            return new Leaf(mostFrequentLabel, depth);

        String mostDiscriminativeFeature = getMostDiscriminativeFeature(set, featuresAvailable);

        Map<String, Dataset> newDatasets = set.splitByFeature(mostDiscriminativeFeature);
        Map<String, TreeNode> subtrees = new HashMap<>();

        for (Map.Entry<String, Dataset> newDataset : newDatasets.entrySet()) {
            Set<String> newFeaturesAvailable = new HashSet<>(featuresAvailable);
            newFeaturesAvailable.remove(mostDiscriminativeFeature);

            TreeNode subtreeRoot = generateDecisionTree(newDataset.getValue(), set, newFeaturesAvailable, depth + 1);
            subtrees.put(newDataset.getKey(), subtreeRoot);
        }

        return new InnerNode(mostDiscriminativeFeature, set.getMostFrequentLabel(), subtrees, depth);
    }

    private String getMostDiscriminativeFeature(Dataset set, Set<String> featuresAvailable) {
        requireNonNull(set, "The given set cannot be null!");
        requireNonNull(featuresAvailable, "The given list of available features cannot be null!");

        return set.getInformationGains().entrySet().stream()
                .filter(entry -> featuresAvailable.contains(entry.getKey()))
                .max((gain1, gain2) -> gain1.getValue().equals(gain2.getValue()) ?
                        gain2.getKey().compareTo(gain1.getKey()) :
                        gain1.getValue().compareTo(gain2.getValue())).orElseThrow().getKey();
    }

    private String getPrediction(Example example, TreeNode node) {
        if (node instanceof Leaf) return node.toString();
        InnerNode innerNode = (InnerNode) node;

        Optional<String> exampleFeatureValue = example.getFeatureValue(innerNode.getFeature());
        if (exampleFeatureValue.isEmpty()) throw new DecisionTreeException();

        Map<String, TreeNode> subtrees = innerNode.getSubtrees();
        if (!subtrees.containsKey(exampleFeatureValue.get())) return innerNode.getMostFrequentClassLabel();
        else return getPrediction(example, subtrees.get(exampleFeatureValue.get()));
    }

    private double calculateAccuracy(List<String> predictions, Dataset testSet) {
        List<String> expectedClassLabels = new ArrayList<>();
        testSet.getExamplesIterator().forEachRemaining(example -> expectedClassLabels.add(example.getClassLabel()));

        int correct = 0;
        int total = predictions.size();
        for (int i = 0; i < total; i++) if (expectedClassLabels.get(i).equals(predictions.get(i))) correct++;

        return 1.0 * correct / total;
    }

    private TreeMap<String, TreeMap<String, Integer>> getConfusionMatrix(List<String> predictions, Dataset testSet) {
        requireNonNull(predictions, "The given list of predictions cannot be null!");
        requireNonNull(testSet, "The given test set cannot be null!");

        List<String> testSetClassLabels = testSet.getDistinctLabels();

        TreeMap<String, TreeMap<String, Integer>> confusionMatrix = new TreeMap<>(Comparator.naturalOrder());
        testSetClassLabels.stream().forEachOrdered(label -> confusionMatrix.put(label, new TreeMap<>(Comparator.naturalOrder())));

        for (Iterator<Example> it = testSet.getExamplesIterator(); it.hasNext(); ) {
            String trueClassLabel = it.next().getClassLabel();
            for (Iterator<Example> secIt = testSet.getExamplesIterator(); secIt.hasNext(); ) {
                String predictedClassLabel = secIt.next().getClassLabel();
                if (!confusionMatrix.get(trueClassLabel).containsKey(predictedClassLabel)) {
                    confusionMatrix.get(trueClassLabel).put(predictedClassLabel, 0);
                }
            }
        }

        Iterator<Example> it = testSet.getExamplesIterator();
        for (int i = 0; it.hasNext(); i++) {
            String trueClassLabel = it.next().getClassLabel();
            String predictedClassLabel = predictions.get(i);
            confusionMatrix.get(trueClassLabel).put(predictedClassLabel,
                    confusionMatrix.get(trueClassLabel).get(predictedClassLabel) + 1);
        }

        return confusionMatrix;
    }
}
