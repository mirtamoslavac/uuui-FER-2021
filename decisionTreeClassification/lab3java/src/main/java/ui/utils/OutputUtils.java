package ui.utils;

import ui.nodes.*;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class OutputUtils {
    public static void printBranches(TreeNode root) {
        requireNonNull(root, "The given root node cannot be null!");
        System.out.println("[BRANCHES]:");

        List<List<String>> branches = new ArrayList<>();
        getBranches(root, branches);
        branches.forEach(branch -> System.out.println(String.join(" ", branch)));
    }

    private static void getBranches(TreeNode root, List<List<String>> finalBranches) {
        requireNonNull(root, "The given root node cannot be null!");
        requireNonNull(finalBranches, "The given list of branches cannot be null!");

        ArrayList<String> currentBranch = new ArrayList<>();
        getBranches(root, currentBranch, finalBranches);
    }

    private static void getBranches(TreeNode root, List<String> currentBranch, List<List<String>> finalBranches) {
        requireNonNull(root, "The given root node cannot be null!");
        requireNonNull(currentBranch, "The given current branch cannot be null!");
        requireNonNull(finalBranches, "The given list of branches cannot be null!");

        if (root instanceof Leaf) {
            currentBranch.add(root.toString());
            finalBranches.add(new ArrayList<>(currentBranch));
            currentBranch.remove(currentBranch.size() - 1);
            return;
        }

        ((InnerNode) root).getSubtrees().forEach((key, value) -> {
            if (currentBranch.size() >= 1
                    && currentBranch.get(currentBranch.size() - 1).startsWith(String.valueOf(root.toString().charAt(0))))
                currentBranch.remove(currentBranch.size() - 1);

            currentBranch.add(root + "=" + key);
            getBranches(value, currentBranch, finalBranches);
        });
        currentBranch.remove(currentBranch.size() - 1);
    }

    public static void printPredictions(List<String> predictions) {
        requireNonNull(predictions, "The given list of predictions cannot be null!");

        System.out.print("[PREDICTIONS]: ");
        System.out.println(String.join(" ", predictions));
    }

    public static void printAccuracy(double accuracy) {
        System.out.printf(Locale.US, "[ACCURACY]: %.5f%n", accuracy);
    }

    public static void printConfusionMatrix(Map<String, TreeMap<String, Integer>> confusionMatrix, List<String> distinctLabels) {
        requireNonNull(confusionMatrix, "The given confusion matrix cannot be null!");
        requireNonNull(distinctLabels, "The given list of distinct labels cannot be null!");

        System.out.println("[CONFUSION_MATRIX]: ");
        StringBuilder sb = new StringBuilder();
        distinctLabels.sort(Comparator.naturalOrder());

        for (int i = 0, matrix1Dsize = confusionMatrix.keySet().size(); i < matrix1Dsize; i++) {
            for (int j = 0; j < matrix1Dsize; j++) {
                sb.append(confusionMatrix.get(distinctLabels.get(i)).get(distinctLabels.get(j)));
                if (j != matrix1Dsize - 1) sb.append(" ");
            }
            sb.append("\n");
        }

        System.out.print(sb);
    }
}
