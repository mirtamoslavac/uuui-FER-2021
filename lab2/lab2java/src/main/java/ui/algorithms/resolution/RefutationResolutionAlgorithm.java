package ui.algorithms.resolution;

import ui.algorithms.resolution.strategies.simplification.DeletionSimplificationStrategy;
import ui.clauses.*;
import ui.pairs.UnorderedPair;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class RefutationResolutionAlgorithm implements DeletionSimplificationStrategy {
    private static final String NIL = "NIL";

    protected final List<Clause> inputClauses;
    protected Clause goalClause;
    protected final List<UnorderedPair<Clause, Clause>> resolvedPairs;
    protected Set<Clause> clauses;

    public RefutationResolutionAlgorithm(ClausesCollection listOfClauses) {
        requireNonNull(listOfClauses, "The given collection of clauses cannot be null!");
        inputClauses = listOfClauses.getInputClauses();
        goalClause = listOfClauses.getGoalClause();

        resolvedPairs = new ArrayList<>();
        clauses = new LinkedHashSet<>(inputClauses);
        clauses.addAll(Clause.negate(goalClause));
    }

    public Optional<Resolvent> algorithm() {
        Set<Clause> newClauses = new LinkedHashSet<>();
        while (true) {
            while (true) {
                Optional<UnorderedPair<Clause, Clause>> selectedClauses = selectClauses();
                if (selectedClauses.isEmpty()) break;
                List<Resolvent> resolvents = resolve(selectedClauses.get());
                for (Resolvent resolvent : resolvents) {
                    if (resolvent.getLiterals().stream().map(Literal::getAtom).collect(Collectors.toList()).contains(NIL))
                        return Optional.of(resolvent);
                    newClauses.addAll(resolvents);
                    placeClauses(resolvents);
                    removeClauses();
                }
            }
            if (clauses.containsAll(newClauses)) return Optional.empty();
            clauses.addAll(newClauses);
        }
    }

    protected void removeClauses() {
        removeRedundantClauses(clauses);
        removeIrrelevantValidClauses(clauses);
    }

    protected Optional<UnorderedPair<Clause, Clause>> selectClauses() {
        return selectClauses(clauses, clauses);
    }

    protected Optional<UnorderedPair<Clause, Clause>> selectClauses(Set<Clause> clauses1, Set<Clause> clauses2) {
        requireNonNull(clauses1, "The given set of clauses cannot be null!");
        requireNonNull(clauses2, "The given set of clauses cannot be null!");

        for (Clause clause1 : clauses1) {
            for (Clause clause2 : clauses2) {
                if (clause1.equals(clause2)) continue;

                UnorderedPair<Clause, Clause> clausePair = new UnorderedPair<>(clause1, clause2);
                if (resolvedPairs.contains(clausePair)) continue;
                if (!checkIfResolvable(clausePair)) continue;

                resolvedPairs.add(clausePair);
                return Optional.of(clausePair);
            }
        }
        return Optional.empty();
    }

    protected boolean checkIfResolvable(UnorderedPair<Clause, Clause> clausePair) {
        requireNonNull(clausePair, "The given pair of clauses cannot be null!");
        firstLiteralFor:
        for (Literal literal1 : clausePair.getFirst().getLiterals()) {
            for (Literal literal2 : clausePair.getSecond().getLiterals()) {
                if ((literal1.getAtom().compareTo(literal2.getAtom())) < 0)
                    continue firstLiteralFor;
                if (literal1.getAtom().equals(literal2.getAtom()) && literal1.isNegated() != literal2.isNegated())
                    return true;
            }
        }
        return false;
    }

    private List<Resolvent> resolve(UnorderedPair<Clause, Clause> parentClauses) {
        requireNonNull(parentClauses, "The given pair of parent clauses cannot be null!");
        Clause parent1 = parentClauses.getFirst();
        Clause parent2 = parentClauses.getSecond();

        if (parent1.getLiterals().size() == 1 && parent2.getLiterals().size() == 1
                && parent1.getLiterals().get(0).getAtom().equals(parent2.getLiterals().get(0).getAtom())
                && parent1.getLiterals().get(0).isNegated() != (parent2.getLiterals().get(0).isNegated()))
            return Collections.singletonList(new Resolvent(Collections.singletonList(new Literal(NIL)), parentClauses));

        List<Resolvent> resolvents = new ArrayList<>();

        List<Literal> literal1ToRemove = new ArrayList<>(), literal2ToRemove = new ArrayList<>();
        iteration:
        for (Literal literal1 : parent1.getLiterals()) {
            for (Literal literal2 : parent2.getLiterals()) {
                if ((literal1.getAtom().compareTo(literal2.getAtom())) < 0) continue iteration;
                if (literal1.getAtom().equals(literal2.getAtom()) && literal1.isNegated() != literal2.isNegated()) {
                    literal1ToRemove.add(literal1);
                    literal2ToRemove.add(literal2);
                }
            }
        }

        for (int i = 0, numberOfResolvents = literal1ToRemove.size(); i < numberOfResolvents; i++) {
            List<Literal> newLiterals = new ArrayList<>();
            newLiterals.addAll(parent1.getLiterals());
            newLiterals.addAll(parent2.getLiterals());
            newLiterals.remove(literal1ToRemove.get(i));
            newLiterals.remove(literal2ToRemove.get(i));
            resolvents.add(new Resolvent(newLiterals, parentClauses));
        }

        return resolvents;
    }

    protected void placeClauses(List<Resolvent> resolvents) {
    }
}
