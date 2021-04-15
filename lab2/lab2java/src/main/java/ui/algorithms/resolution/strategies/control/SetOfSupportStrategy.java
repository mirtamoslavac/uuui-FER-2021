package ui.algorithms.resolution.strategies.control;

import ui.algorithms.resolution.RefutationResolutionAlgorithm;
import ui.clauses.*;
import ui.pairs.UnorderedPair;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class SetOfSupportStrategy extends RefutationResolutionAlgorithm {
    private final Set<Clause> setOfSupport;

    public SetOfSupportStrategy(ClausesCollection listOfClauses) {
        super(requireNonNull(listOfClauses, "The given collection of clauses cannot be null!"));

        //remove the added goal in the base refutation resolution
        clauses = new LinkedHashSet<>(inputClauses);
        setOfSupport = new LinkedHashSet<>(Clause.negate(goalClause));
    }

    @Override
    protected Optional<UnorderedPair<Clause, Clause>> selectClauses() {
        return selectClauses(clauses, setOfSupport);
    }

    @Override
    protected void removeClauses() {
        removeRedundantClauses(clauses);
        removeRedundantClauses(setOfSupport);
        removeIrrelevantValidClauses(clauses);
        removeIrrelevantValidClauses(setOfSupport);
    }

    @Override
    protected void placeClauses(List<Resolvent> resolvents) {
        setOfSupport.addAll(requireNonNull(resolvents, "The given resolvents cannot be null!"));
    }

    @Override
    public void removeRedundantClauses(Set<Clause> clauses) {
        Set<Clause> clausesToIterateOver = new LinkedHashSet<>(requireNonNull(clauses, "The given set of clauses cannot be null!"));

        for (Clause clause1 : clausesToIterateOver) {
            for (Clause clause2 : clausesToIterateOver) {
                if (clause1.equals(clause2)) continue;

                if (clause2.getLiterals().containsAll(clause1.getLiterals())) {
                    clauses.remove(clause2);
                    if (clause2 instanceof Resolvent) setOfSupport.remove(clause2);
                }
            }
        }
    }
}
