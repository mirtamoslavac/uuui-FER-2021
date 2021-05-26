package ui.algorithms;

import ui.NodeHeuristic;
import ui.utils.OutputUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static java.util.Objects.requireNonNull;

public class HeuristicAlgorithm extends GeneralSearch {
    static HashMap<String, NodeHeuristic> closed;
    static NodeHeuristic resultNode = null;
    static List<NodeHeuristic> path;
    static boolean pathExisting = false;

    public static Map<String, Double> heuristicFunctions = new HashMap<>();

    public static void missingArgumentCheck(String hdPath) {
        if (hdPath == null) throw new IllegalArgumentException("The heuristic descriptor path was not specified!");
    }

    public static void parseAndRunAlgorithm(String algorithmAbbreviation, String ssdPath, String hdPath, boolean consistentFlag, boolean optimisticFlag, int minHDRows) throws IOException {
        requireNonNull(ssdPath, "The given space state descriptor path cannot be null!");
        requireNonNull(hdPath, "The given heuristic descriptor path cannot be null!");

        StringBuilder initStateSB = new StringBuilder();
        List<String> goalStates = new ArrayList<>();
        Map<String, Map<String, Integer>> stateTransitionsSorted = new TreeMap<>(Comparator.naturalOrder());

        parseStateSpaceDescriptor(ssdPath, minHDRows, initStateSB, goalStates, stateTransitionsSorted);
        Map<String, Map<String, Integer>> stateTransitions = new HashMap<>(stateTransitionsSorted);
        String initState = initStateSB.toString();

        heuristicFunctions = new HashMap<>();
        parseHeuristicDescriptor(hdPath, heuristicFunctions, minHDRows);

        if (consistentFlag) {
            OutputUtils.printHeader("heuristic-consistent", hdPath);
            HeuristicAlgorithm.checkIfConsistent(stateTransitionsSorted);
        } else if (optimisticFlag) {
            OutputUtils.printHeader("heuristic-optimistic", hdPath);
            HeuristicAlgorithm.checkIfOptimistic(stateTransitions, goalStates);
        } else {
            if (algorithmAbbreviation != null) {
                switch (algorithmAbbreviation) {
                    case "astar":
                        OutputUtils.printHeader("a-star", hdPath);
                        AStarSearch.algorithm(initState, stateTransitions, goalStates);
                        if (AStarSearch.checkIfSolutionFound())
                            OutputUtils.printAlgorithmResult(true, AStarSearch.getNumberOfStatesVisited(), AStarSearch.getPathLength(), AStarSearch.getTotalCost(), AStarSearch.getPath());
                        else OutputUtils.printAlgorithmResult(false);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private static void checkIfConsistent(Map<String, Map<String, Integer>> stateTransitions) {
        requireNonNull(stateTransitions, "The given map of state transitions cannot be null!");

        AtomicBoolean passed = new AtomicBoolean(true);

        for (Map.Entry<String, Map<String, Integer>> transition : stateTransitions.entrySet()) {
            transition.getValue().forEach(((String child, Integer cost) -> {
                OutputUtils.printHeuristicCheckRowBeginning(heuristicFunctions.get(transition.getKey()) <= heuristicFunctions.get(child) + cost);
                OutputUtils.printHeuristicCheckRowEnd(transition.getKey(), child, heuristicFunctions.get(transition.getKey()), heuristicFunctions.get(child), (double) cost);
                if (passed.get() && heuristicFunctions.get(transition.getKey()) > heuristicFunctions.get(child) + cost)
                    passed.set(false);
            }));
        }

        OutputUtils.printHeuristicCheckResult(passed.get(), "consistent");
    }

    private static void checkIfOptimistic(Map<String, Map<String, Integer>> stateTransitions, List<String> goalStates) {
        requireNonNull(stateTransitions, "The given map of state transitions cannot be null!");
        requireNonNull(goalStates, "The given list of goal states cannot be null!");

        List<String> allStates = heuristicFunctions.keySet().stream().sorted(Comparator.naturalOrder()).distinct().collect(Collectors.toList());
        boolean passed = true;

        for (String state : allStates) {
            resultNode = null;
            path = null;
            pathExisting = false;
            UniformCostSearch.algorithm(state, stateTransitions, goalStates);
            OutputUtils.printHeuristicCheckRowBeginning(heuristicFunctions.get(state) <= UniformCostSearch.getTotalCost());
            OutputUtils.printHeuristicCheckRowEnd(state, heuristicFunctions.get(state), UniformCostSearch.getTotalCost());
            if (passed && heuristicFunctions.get(state) > UniformCostSearch.getTotalCost()) passed = false;
        }

        OutputUtils.printHeuristicCheckResult(passed, "optimistic");
    }

    private static void parseHeuristicDescriptor(String hdPath, Map<String, Double> heuristicFunctions, int minHDRows) throws IOException {
        requireNonNull(hdPath, "The given heuristic descriptor path cannot be null!");
        requireNonNull(heuristicFunctions, "The given map of heuristic functions cannot be null!");

        List<String> rows = new ArrayList<>();
        String row;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(hdPath), StandardCharsets.UTF_8))) {
            while ((row = reader.readLine()) != null) {
                if (row.startsWith("#")) continue;
                rows.add(row);
            }
            if (rows.size() < minHDRows)
                throw new IllegalArgumentException("The heuristic descriptor must contain at least one non-comment line!");

            rows.forEach(line -> {
                String[] pair = line.strip().split(":");
                heuristicFunctions.put(pair[0], parseDouble(pair[1].strip()));
            });
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Cannot find a file at the given path!");
        } catch (IOException e) {
            throw new IOException("Cannot read from the given path!");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot parse given cost into a number!");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
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
        return requireNonNull(resultNode).getF();
    }

    public static String getPath() {
        if (!pathExisting) createPath();

        return path.stream().map(NodeHeuristic::getState).collect(Collectors.joining(" => "));
    }

    private static void createPath() {
        NodeHeuristic currentNode = resultNode;
        path = new ArrayList<>();
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getParentNode();
        }

        Collections.reverse(path);
        pathExisting = true;
    }
}
