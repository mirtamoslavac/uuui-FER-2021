package ui.algorithms;

import ui.Node;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class UniformCostSearch extends BlindAlgorithm {

    public static void algorithm(String initialState, Map<String, Map<String, Integer>> successorFunction, List<String> goalStates) {
        requireNonNull(initialState, "The given initial state cannot be null!");
        requireNonNull(successorFunction, "The map of successor functions cannot be null!");
        requireNonNull(goalStates, "The given list of goal states cannot be null!");

        PriorityQueue<Node> open = new PriorityQueue<>();
        closed = new HashSet<>();

        open.add(new Node(initialState, null, 0));

        while (!open.isEmpty()) {
            Node headNode = open.remove();

            closed.add(headNode.getState());
            if (goalStates.contains(headNode.getState())) {
                resultNode = headNode;
                return;
            }

            for (Map.Entry<String, Integer> childStateAndCost : successorFunction.get(headNode.getState()).entrySet()) {
                if (!closed.contains(childStateAndCost.getKey())) {
                   open.add(new Node(childStateAndCost.getKey(), headNode, childStateAndCost.getValue()));
                }
            }
        }
    }
}
