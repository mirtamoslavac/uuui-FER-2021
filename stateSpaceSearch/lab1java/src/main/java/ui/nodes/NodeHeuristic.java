package ui;

import ui.algorithms.HeuristicAlgorithm;

import java.util.Objects;

import static java.lang.Double.max;

public class NodeHeuristic extends Node {
    private final NodeHeuristic parent;
    private final double fFunction;
    private final double gFunction;

    public NodeHeuristic(String state, NodeHeuristic parent, int cost) {
        super(state, parent, cost);
        this.parent = parent;

        this.gFunction = (this.parent != null) ? this.parent.getG() + cost : cost;
        this.fFunction = this.gFunction + HeuristicAlgorithm.heuristicFunctions.get(this.getState());
    }

    public String getState() {
        return this.state;
    }

    public double getG() {
        return this.gFunction;
    }

    public double getF() {
        return this.fFunction;
    }

    public NodeHeuristic getParentNode() {
        return this.parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeHeuristic node = (NodeHeuristic) o;
        return Double.compare(node.fFunction, fFunction) == 0 && Double.compare(node.gFunction, gFunction) == 0 && state.equals(node.state) && Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, parent, fFunction, gFunction);
    }

    @Override
    public int compareTo(Node other) {
        NodeHeuristic o = (NodeHeuristic) other;
        if (this.getF() == o.getF()) return this.getState().compareTo(o.getState());
        else return Double.compare(this.getF(), o.getF());
    }
}
