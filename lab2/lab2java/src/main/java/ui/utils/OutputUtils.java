package ui.utils;

import ui.clauses.*;
import ui.commands.UserCommand;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

public class OutputUtils {
    private static final String CONCLUSION = "[CONCLUSION]: ";
    private static final String SEPARATOR = "===============";

    public static void printConclusion(String goalClause) {
        requireNonNull(goalClause, "The given goal clause string cannot be null!");

        System.out.println(CONCLUSION + goalClause + " is unknown");
    }

    public static void printConclusion(String goalClause, Resolvent resultingClause) {
        requireNonNull(goalClause, "The given goal clause string cannot be null!");
        requireNonNull(resultingClause, "The given resulting clause cannot be null!");

        Set<Clause> initialClausesAndNegatedGoalUsed = new TreeSet<>(Comparator.naturalOrder());
        Set<Resolvent> resolventsUsedSet = new LinkedHashSet<>();
        getClauses(resultingClause, initialClausesAndNegatedGoalUsed, resolventsUsedSet);
        List<Resolvent> resolventsUsedList = new ArrayList<>(resolventsUsedSet);
        Collections.reverse(resolventsUsedList);
        resolventsUsedList.add(resultingClause);

        AtomicInteger ordinalNumber = new AtomicInteger();
        Map<Clause, Integer> ordinals = new HashMap<>();
        initialClausesAndNegatedGoalUsed.stream().forEachOrdered(clause -> ordinals.put(clause, ordinalNumber.incrementAndGet()));
        resolventsUsedList.stream().forEachOrdered(clause -> ordinals.put(clause, ordinalNumber.incrementAndGet()));

        StringBuilder sb = new StringBuilder();
        initialClausesAndNegatedGoalUsed.stream().forEachOrdered(clause -> sb.append(ordinals.get(clause)).append(". ")
                .append(clause.toString()).append("\n"));
        sb.append(SEPARATOR).append("\n");
        resolventsUsedList.stream().forEachOrdered(clause -> sb.append(ordinals.get(clause)).append(". ")
                .append(clause.toString()).append(" (").append(ordinals.get(clause.getParentClauses().getFirst()))
                .append(", ").append(ordinals.get(clause.getParentClauses().getSecond())).append(")").append("\n"));
        sb.append(SEPARATOR).append("\n").append(CONCLUSION).append(goalClause).append(" is true");

        System.out.println(sb);
    }

    private static void getClauses(Clause clause, Set<Clause> initialClausesAndNegatedGoalUsed, Set<Resolvent> resolventsUsed) {
        requireNonNull(clause, "The given clause cannot be null!");
        requireNonNull(initialClausesAndNegatedGoalUsed, "The given set of initial clauses cannot be null!");
        requireNonNull(resolventsUsed, "The given set of resolvents cannot be null!");

        try {
            Clause parent1 = ((Resolvent) clause).getParentClauses().getFirst();
            Clause parent2 = ((Resolvent) clause).getParentClauses().getSecond();

            if (!(parent1 instanceof Resolvent) && !(parent2 instanceof Resolvent)) {
                initialClausesAndNegatedGoalUsed.add(parent2);
                initialClausesAndNegatedGoalUsed.add(parent1);
            } else {
                if (parent1 instanceof Resolvent) resolventsUsed.add((Resolvent) parent1);
                else initialClausesAndNegatedGoalUsed.add(parent1);
                if (parent2 instanceof Resolvent) resolventsUsed.add((Resolvent) parent2);
                else initialClausesAndNegatedGoalUsed.add(parent2);
            }

            getClauses(parent1, initialClausesAndNegatedGoalUsed, resolventsUsed);
            getClauses(parent2, initialClausesAndNegatedGoalUsed, resolventsUsed);
        } catch (ClassCastException e) {
            return;
        }
    }

    public static void printKnowledge(ClausesCollection listOfClauses) {
        requireNonNull(listOfClauses, "The given collection of clauses cannot be null!");

        System.out.println("Constructed with knowledge:");
        listOfClauses.getInputClauses().forEach(System.out::println);
        System.out.println(listOfClauses.getGoalClause());
    }

    public static void printUserCommand(UserCommand userCommand) {
        requireNonNull(userCommand, "The given user command cannot be null!");
        System.out.println("\nUser's command: " + userCommand);
    }

    public static void printClauseAdditionSuccess(Clause clause) {
        requireNonNull(clause, "The given clause cannot be null!");

        System.out.println("\nAdded \"" + clause + "\" to the knowledge database!");
    }

    public static void printClauseAdditionFailure(Clause clause) {
        requireNonNull(clause, "The given clause cannot be null!");

        System.out.println("\nNothing added since \"" + clause + "\" already is a part of the current knowledge!");
    }

    public static void printClauseRemovalSuccess(Clause clause) {
        requireNonNull(clause, "The given clause cannot be null!");

        System.out.println("\nRemoved \"" + clause+ "\" from the knowledge database!");
    }

    public static void printClauseRemovalFailure(Clause clause) {
        requireNonNull(clause, "The given clause cannot be null!");

        System.out.println("\nNothing removed since \"" + clause + "\" is not a part of the current knowledge!");
    }

    public static void printQuery(UserCommand userCommand, String goalClause) {
        requireNonNull(userCommand, "The given user command cannot be null!");
        requireNonNull(goalClause, "The given goal clause string cannot be null!");

        printUserCommand(userCommand);
        printConclusion(goalClause);
    }

    public static void printQuery(UserCommand userCommand, String goalClause, Resolvent resultingClause) {
        requireNonNull(userCommand, "The given user command cannot be null!");
        requireNonNull(goalClause, "The given goal clause string cannot be null!");
        requireNonNull(resultingClause, "The given resulting clause cannot be null!");

        printUserCommand(userCommand);
        printConclusion(goalClause, resultingClause);
    }
}
