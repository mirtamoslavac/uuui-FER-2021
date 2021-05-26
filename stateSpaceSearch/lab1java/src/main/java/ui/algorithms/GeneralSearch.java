package ui.algorithms;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;

public class GeneralSearch {

    public static void parseStateSpaceDescriptor(String ssdPath, int minRows, StringBuilder initState, List<String> goalStates, Map<String, Map<String, Integer>> stateTransitions) throws IOException {
        requireNonNull(ssdPath, "The given state space descriptor cannot be null!");
        requireNonNull(initState, "The given StringBuilder instance cannot be null!");
        requireNonNull(goalStates, "The given list of goal states cannot be null!");
        requireNonNull(stateTransitions, "The given map of state transitions cannot be null!");

        List<String> rows = new ArrayList<>();
        String row;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(ssdPath), StandardCharsets.UTF_8))) {
            while ((row = reader.readLine()) != null) {
                if (row.startsWith("#")) continue;
                rows.add(row);
            }
            if (rows.size() < minRows) throw new IllegalArgumentException("The state space descriptor must contain at least three non-comment lines, got " + rows.size() + "!");

            initState.append(rows.get(0).strip());

            goalStates.addAll(Arrays.stream(rows.get(1).strip().split(" ")).collect(Collectors.toList()));

            parseStateTransitions(rows, stateTransitions);

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

    private static void parseStateTransitions(List<String> rows, Map<String, Map<String, Integer>> transitions) {
        requireNonNull(rows, "The given list of rows cannot be null!");
        requireNonNull(transitions, "The given map of state transitions cannot be null!");

        for (int i = 2, rowsSize = rows.size(); i < rowsSize; i++) {
            String[] transition = rows.get(i).strip().split(":");
            if (transition.length < 2) continue;
            String[] next = transition[1].strip().split(" ");
            Map<String, Integer> nextPairs = new TreeMap<>(Comparator.naturalOrder());
            Arrays.stream(next).forEach(nextPair -> {
                String[] nextPairArr = nextPair.split(",");
                nextPairs.put(nextPairArr[0], parseInt(nextPairArr[1]));
            });
            transitions.put(transition[0], nextPairs);
        }
    }

}
