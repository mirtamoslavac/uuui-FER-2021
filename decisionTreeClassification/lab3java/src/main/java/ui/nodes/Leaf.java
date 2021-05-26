package ui.nodes;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Leaf extends TreeNode {
    private final String nodeClassLabel;

    public Leaf(String classLabel, int depth) {
        super(depth);

        nodeClassLabel = requireNonNull(classLabel, "The given class label cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaf leaf = (Leaf) o;
        return nodeClassLabel.equals(leaf.nodeClassLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeClassLabel);
    }

    @Override
    public String toString() {
        return nodeClassLabel;
    }
}
