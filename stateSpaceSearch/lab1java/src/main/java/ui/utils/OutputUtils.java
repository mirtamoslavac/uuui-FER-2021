package ui;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class OutputUtils {

    public static void printHeader(String... arguments) {
        requireNonNull(arguments, "The given array of String arguments cannot be null!");

        if (arguments.length < 1) throw new IllegalArgumentException("Invalid number of arguments for header construction!");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arguments.length; i++)
            sb.append(" ").append(i != 0 ? Objects.requireNonNull(arguments[i]) : Objects.requireNonNull(arguments[i]).toUpperCase());

        System.out.println("#" + sb.toString());
    }

    public static void printAlgorithmResult(boolean solutionFound, int numberOfStatesVisited, int pathLength, double totalCost, String path) {
        requireNonNull(path, "The given heuristic descriptor path cannot be null!");
        StringBuilder sb = new StringBuilder();

        sb.append("[FOUND_SOLUTION]: ").append(solutionFound ? "yes" : "no").append("\n")
                .append("[STATES_VISITED]: ").append(numberOfStatesVisited).append("\n")
                .append("[PATH_LENGTH]: ").append(pathLength).append("\n")
                .append("[TOTAL_COST]: ").append(String.format(Locale.US,"%.1f", totalCost)).append("\n")
                .append("[PATH]: ").append(path);

        System.out.println(sb.toString());
    }

    public static void printAlgorithmResult(boolean solutionFound) {
        System.out.println("[FOUND_SOLUTION]: " + (solutionFound ? "yes" : "no"));
    }

    public static void printHeuristicCheckRowBeginning(boolean ok) {
        System.out.print("[CONDITION]: [" + (ok ? "OK" : "ERR") + "] ");
    }

    public static void printHeuristicCheckRowEnd(String state, double stateHeuristicValue, double stateTrueCostValue) {
        requireNonNull(state, "The given state cannot be null!");

        System.out.println("h(" + state + ") <= h*: "
                + String.format(Locale.US,"%.1f", stateHeuristicValue)
                + " <= " + String.format(Locale.US,"%.1f", stateTrueCostValue));
    }

    public static void printHeuristicCheckRowEnd(String state1, String state2, double state1HeuristicValue, double state2HeuristicValue, double c) {
        requireNonNull(state1, "The given parent state cannot be null!");
        requireNonNull(state2, "The given child state cannot be null!");

        System.out.println("h(" + state1 + ") <= h(" + state2 + ") + c: "
                + String.format(Locale.US,"%.1f", state1HeuristicValue)
                + " <= " + String.format(Locale.US,"%.1f", state2HeuristicValue)
                + " + " + String.format(Locale.US,"%.1f", c));
    }

    public static void printHeuristicCheckResult(boolean passed, String type) {
        requireNonNull(type, "The given heuristic check cannot be null!");

        System.out.println("[CONCLUSION]: Heuristic is " + (!passed ? "not " : "") + type + ".");
    }
}
