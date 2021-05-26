package ui.algorithms.resolution.strategies.simplification;

import ui.clauses.*;

import java.util.*;

import static java.util.Objects.requireNonNull;

public interface DeletionSimplificationStrategy {
    default void removeRedundantClauses(Set<Clause> clauses) {
        Set<Clause> clausesToIterateOver = new LinkedHashSet<>(requireNonNull(clauses, "The given set of clauses cannot be null!"));

        for (Clause clause1 : clausesToIterateOver) {
            for (Clause clause2 : clausesToIterateOver) {
                if (clause1.equals(clause2)) continue;

                if (clause2.getLiterals().containsAll(clause1.getLiterals()))
                    clauses.remove(clause2);
            }
        }
    }

    default void removeIrrelevantValidClauses(Set<Clause> clauses) {
        List<Clause> clausesToIterateOver = new ArrayList<>(requireNonNull(clauses, "The given set of clauses cannot be null!"));
        iteration:
        for (Clause clause : clausesToIterateOver) {
            literal1:
            for (Literal literal1 : clause.getLiterals())
                for (Literal literal2 : clause.getLiterals()) {
                    if ((literal1.getAtom().compareTo(literal2.getAtom())) < 0) continue literal1;

                    if (literal1.getAtom().equals(literal2.getAtom()) && literal1.isNegated() != literal2.isNegated()) {
                        clauses.remove(clause);
                        continue iteration;
                    }
                }
        }
    }
}
