package ui.algorithms;

import ui.Node;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class BreadthFirstSearch extends BlindAlgorithm {

    public static void algorithm(String initialState, Map<String, Map<String, Integer>> successorFunction, List<String> goalStates) {
        requireNonNull(initialState, "The given initial state cannot be null!");
        requireNonNull(successorFunction, "The map of successor functions cannot be null!");
        requireNonNull(goalStates, "The given list of goal states cannot be null!");

        List<Node> open = new ArrayList<>();
        closed = new HashSet<>();

        open.add(new Node(initialState, null, 0));

        while (!open.isEmpty()) {
            Node headNode = open.remove(0);
            closed.add(headNode.getState());

            if (goalStates.contains(headNode.getState())) {
                resultNode = headNode;
                return;
            }

            List<Node> children = new ArrayList<>();
            for (Map.Entry<String, Integer> childStateAndCost : successorFunction.get(headNode.getState()).entrySet()) {
                if (!closed.contains(childStateAndCost.getKey())) {
                    Node childNode = new Node(childStateAndCost.getKey(), headNode, childStateAndCost.getValue());
                    children.add(childNode);
                }
            }
            children.sort(Comparator.comparing(Node::getState));
            open.addAll(children);
        }
    }
}
