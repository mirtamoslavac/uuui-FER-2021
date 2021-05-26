package ui.clauses;

import ui.pairs.UnorderedPair;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class Resolvent extends Clause {
    private final UnorderedPair<Clause, Clause> parentClauses;

    public Resolvent(List<Literal> literals, UnorderedPair<Clause, Clause> parentClauses) {
        super(requireNonNull(literals, "The given literals cannot be null!"));
        this.parentClauses = requireNonNull(parentClauses, "The given pair of parent clauses cannot be null!");
    }

    public UnorderedPair<Clause, Clause> getParentClauses() {
        return parentClauses;
    }
}
