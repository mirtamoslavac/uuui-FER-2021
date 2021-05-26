package ui.algorithms;

import ui.NodeHeuristic;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class AStarSearch extends HeuristicAlgorithm {

    public static void algorithm(String initialState, Map<String, Map<String, Integer>> successorFunction, List<String> goalStates) {
        requireNonNull(initialState, "The given initial state cannot be null!");
        requireNonNull(successorFunction, "The map of successor functions cannot be null!");
        requireNonNull(goalStates, "The given list of goal states cannot be null!");

        PriorityQueue<NodeHeuristic> open = new PriorityQueue<>();
        HashMap<String, NodeHeuristic> openStates = new HashMap<>();

        open.add(new NodeHeuristic(initialState, null, 0));
        openStates.put(initialState, open.peek());
        closed = new HashMap<>();

        while (!open.isEmpty()) {
            NodeHeuristic headNode = open.remove();

            closed.put(headNode.getState(), headNode);
            if (goalStates.contains(headNode.getState())) {
                resultNode = headNode;
                return;
            }

            for (Map.Entry<String, Integer> childStateAndCost : successorFunction.get(headNode.getState()).entrySet()) {
                if (openStates.containsKey(childStateAndCost.getKey())) {
                    NodeHeuristic m = openStates.get(childStateAndCost.getKey());
                    if (m.getG() < headNode.getG() + childStateAndCost.getValue()) continue;
                    else {
                        open.remove(m);
                        openStates.remove(m.getState());
                    }
                } else if (closed.containsKey(childStateAndCost.getKey())) {
                    NodeHeuristic m = closed.get(childStateAndCost.getKey());
                    if (m.getG() < headNode.getG() + childStateAndCost.getValue()) continue;
                    else closed.remove(m.getState());
                }
                open.add(new NodeHeuristic(childStateAndCost.getKey(), headNode, childStateAndCost.getValue()));
            }
        }
    }
}
