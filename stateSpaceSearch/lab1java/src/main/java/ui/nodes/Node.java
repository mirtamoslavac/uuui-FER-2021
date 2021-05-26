package ui;

import java.util.Objects;

public class Node implements Comparable<Node>{
    String state;
    private final Node parent;
    private final int cost;
    private final int totalCost;
    private final int depth;

    public Node(String state, Node parent, int cost) {
        this.state = Objects.requireNonNull(state);
        this.parent = parent;
        this.cost = parent == null ? cost : this.parent.cost + cost;
        this.depth = parent == null ? 0 : this.parent.depth + 1;
        this.totalCost = parent == null ? 0 : this.parent.totalCost + 1;
    }

    public String getState() {
        return this.state;
    }

    public Node getParentNode() {
        return this.parent;
    }

    public int getCost() {
        return this.cost;
    }

    public int getTotalCost() {
        return this.totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return state.equals(node.state) && Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, parent);
    }

    @Override
    public int compareTo(Node o) {
        if (this.getCost() == o.getCost()) return this.getState().compareTo(o.getState());
        else return Integer.compare(this.cost, o.cost);
    }
}
