package ui.algorithms;

import ui.Node;
import ui.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class BlindAlgorithm extends GeneralSearch {
    static HashSet<String> closed;
    static Node resultNode = null;
    static List<Node> path;
    static boolean pathExisting = false;

    public static void excessiveArgumentsCheck(String algorithm, String hdPath, Boolean optimisticFlagPresent, Boolean consistentFlagPresent) {
        if (hdPath != null)
            throw new IllegalArgumentException("No heuristic descriptor path is needed for the " + algorithm + " algorithm!");
        if (optimisticFlagPresent)
            throw new IllegalArgumentException("Cannot check heuristic optimism for the " + algorithm + " algorithm!");
        if (consistentFlagPresent)
            throw new IllegalArgumentException("Cannot check heuristic consistency for the " + algorithm + " algorithm!");
    }

    public static void parseAndRunAlgorithm(String algorithmAbbreviation, String ssdPath, int minSSDRows) throws IOException {
        requireNonNull(ssdPath, "The given state space descriptor cannot be null!");
        requireNonNull(algorithmAbbreviation, "The given algorithm abbreviation cannot be null!");

        StringBuilder initStateSB = new StringBuilder();
        List<String> goalStates = new ArrayList<>();
        Map<String, Map<String, Integer>> stateTransitions = new HashMap<>();

        parseStateSpaceDescriptor(ssdPath, minSSDRows, initStateSB, goalStates, stateTransitions);
        String initState = initStateSB.toString();

        switch (algorithmAbbreviation) {
            case "bfs":
                Utils.printHeader(algorithmAbbreviation);
                BreadthFirstSearch.algorithm(initState, stateTransitions, goalStates);
                if (BlindAlgorithm.checkIfSolutionFound()) Utils.printAlgorithmResult(true, BlindAlgorithm.getNumberOfStatesVisited(), BlindAlgorithm.getPathLength(), BlindAlgorithm.getTotalCost(), BlindAlgorithm.getPath());
                else Utils.printAlgorithmResult(false);
                break;
            case "ucs":
                Utils.printHeader(algorithmAbbreviation);
                UniformCostSearch.algorithm(initState, stateTransitions, goalStates);
                if (BlindAlgorithm.checkIfSolutionFound()) Utils.printAlgorithmResult(true, BlindAlgorithm.getNumberOfStatesVisited(), BlindAlgorithm.getPathLength(), BlindAlgorithm.getTotalCost(), BlindAlgorithm.getPath());
                else Utils.printAlgorithmResult(false);
                break;
            default:
                break;
        }

    }

    public static boolean checkIfSolutionFound() {
        return resultNode != null;
    }

    public static int getNumberOfStatesVisited() {
        return closed.size();
    }

    public static int getPathLength() {
        if (!pathExisting) createPath();
        return path.size();
    }

    public static double getTotalCost() {
        return resultNode.getCost();
    }

    public static String getPath() {
        if (!pathExisting) createPath();

        return path.stream().map(Node::getState).collect(Collectors.joining(" => "));
    }

    private static void createPath() {
        Node currentNode = resultNode;
        path = new ArrayList<>();
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getParentNode();
        }

        Collections.reverse(path);
        pathExisting = true;
    }


}
