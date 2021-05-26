package ui;

import ui.algorithms.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;

import static java.util.Objects.requireNonNull;

public class Solution {
    /**
     * Parameter specifier for the abbreviation for state space search algorithm.
     */
    private static final String SEARCH_ALGORITHM_SPECIFIER = "--alg";
    /**
     * Set containing algorithms that are supported within the program.
     */
    private static final Set<String> SupportedAlgorithms = Stream.of("bfs", "ucs", "astar").collect(Collectors.toCollection(HashSet::new));
    /**
     * Parameter specifier for the path to state space descriptor file.
     */
    private static final String SSD_PATH_HYPHEN = "--ss";
    /**
     * Parameter specifier for the path to heuristic descriptor file.
     */
    private static final String HD_PATH_SPECIFIER = "--h";
    /**
     * Flag for checking if given heuristic is optimistic.
     */
    private static final String OPTIMISTIC_FLAG = "--check-optimistic";
    /**
     * Flag for checking if given heuristic is consistent.
     */
    private static final String CONSISTENT_FLAG = "--check-consistent";

    /**
     * Minimum number of rows within the state space descriptor that are to be processed.
     */
    private static final int SSD_MIN_ROWS = 3;
    /**
     * Minimum number of rows within the heuristic descriptor that are to be processed.
     */
    private static final int HD_MIN_ROWS = 1;

    public static void main(String[] args) {
        String algorithmAbbreviation = null, ssdPath = null, hdPath = null;
        boolean optimisticFlag = false, consistentFlag = false;

        try {
            for (int i = 0, length = args.length; i < length; i++) {
                switch (args[i]) {
                    case SEARCH_ALGORITHM_SPECIFIER:
                        if (algorithmAbbreviation == null) algorithmAbbreviation = args[++i];
                        else throwArgumentException(SEARCH_ALGORITHM_SPECIFIER);
                        break;
                    case SSD_PATH_HYPHEN:
                        if (ssdPath == null) ssdPath = args[++i];
                        else throwArgumentException(SSD_PATH_HYPHEN);
                        break;
                    case HD_PATH_SPECIFIER:
                        if (hdPath == null) hdPath = args[++i];
                        else throwArgumentException(HD_PATH_SPECIFIER);
                        break;
                    case OPTIMISTIC_FLAG:
                        if (!optimisticFlag) optimisticFlag = true;
                        break;
                    case CONSISTENT_FLAG:
                        if (!consistentFlag) consistentFlag = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid parameter \"" + args[i] + "\"!");
                }
            }

            if (algorithmAbbreviation == null && !optimisticFlag && !consistentFlag)
                throw new IllegalArgumentException("The search algorithm to be used was not specified!");
            if (algorithmAbbreviation != null && (optimisticFlag || consistentFlag))
                throw new IllegalArgumentException("Cannot perform a heuristics check when an algorithm is given!");
            if (algorithmAbbreviation != null && !SupportedAlgorithms.contains(algorithmAbbreviation))
                throw new IllegalArgumentException("Invalid search algorithm!");
            if (ssdPath == null)
                throw new IllegalArgumentException("The state space descriptor path was not specified!");

        } catch (NumberFormatException e) {
            System.err.println("Invalid parameter value: " + e.getMessage());
            return;
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println(e.getMessage());
            return;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Missing parameter value within the given arguments \"" + String.join(" ", args) + "\"!");
            return;
        }

        try {
            if (consistentFlag || optimisticFlag) {
                HeuristicAlgorithm.parseAndRunAlgorithm(null, ssdPath, hdPath, consistentFlag, optimisticFlag, HD_MIN_ROWS);
            } else {
                switch (algorithmAbbreviation) {
                    case "bfs", "ucs" -> {
                        BlindAlgorithm.excessiveArgumentsCheck(algorithmAbbreviation.equals("bfs") ? "breadth first search" : "uniform cost search", hdPath, optimisticFlag, consistentFlag);
                        try {
                            BlindAlgorithm.parseAndRunAlgorithm(algorithmAbbreviation, Paths.get(ssdPath).toAbsolutePath().normalize().toString(), SSD_MIN_ROWS);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    default -> {
                        HeuristicAlgorithm.missingArgumentCheck(hdPath);
                        try {
                            HeuristicAlgorithm.parseAndRunAlgorithm(algorithmAbbreviation,
                                    Paths.get(ssdPath).toAbsolutePath().normalize().toString(), Paths.get(hdPath).toAbsolutePath().normalize().toString(),
                                    false, false, HD_MIN_ROWS);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void throwArgumentException(String parameter) {
        requireNonNull(parameter, "The given parameter cannot be null!");
        throw new IllegalArgumentException("Cannot specify the same parameter \"" + parameter.substring(2, parameter.length() - 1) + "\" twice!");
    }
}
