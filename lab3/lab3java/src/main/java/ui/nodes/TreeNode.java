package ui.nodes;

public abstract class TreeNode {
    private int depth;

    public TreeNode(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public abstract String toString();
}
