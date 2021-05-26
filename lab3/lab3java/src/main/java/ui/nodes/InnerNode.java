package ui.nodes;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class InnerNode extends TreeNode {
    private final String feature;
    private final String mostFrequentClassLabel;
    private final Map<String, TreeNode> subtrees;

    public InnerNode(String feature, String mostFrequentClassLabel, Map<String, TreeNode> subtrees, int depth) {
        super(depth);
        this.feature = requireNonNull(feature, "The given feature cannot be null!");
        this.mostFrequentClassLabel = requireNonNull(mostFrequentClassLabel, "The given most frequent class label cannot be null!");
        this.subtrees = requireNonNull(subtrees, "The given subtrees cannot be null!");
    }

    public String getFeature() {
        return feature;
    }

    public Map<String, TreeNode> getSubtrees() {
        return subtrees;
    }

    public String getMostFrequentClassLabel() {
        return mostFrequentClassLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerNode innerNode = (InnerNode) o;
        return feature.equals(innerNode.feature) && subtrees.equals(innerNode.subtrees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feature, subtrees);
    }

    @Override
    public String toString() {
        return (getDepth() + 1) + ":" + feature;
    }
}
