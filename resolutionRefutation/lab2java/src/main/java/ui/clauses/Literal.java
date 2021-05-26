package ui.clauses;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Literal implements Comparable<Literal> {
    public static final String NEGATION_SYMBOL = "~";

    private final String atom;
    private final boolean negated;

    public Literal(String literal) {
        requireNonNull(literal, "The given literal cannot be null!");
        if (literal.startsWith(NEGATION_SYMBOL)) {
            atom = literal.substring(1);
            negated = true;
        } else {
            atom = literal;
            negated = false;
        }
    }

    public String getAtom() {
        return atom;
    }

    public boolean isNegated() {
        return negated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return negated == literal.negated && atom.equals(literal.atom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atom, negated);
    }

    @Override
    public String toString() {
        return isNegated() ? NEGATION_SYMBOL + atom : atom;
    }

    @Override
    public int compareTo(Literal o) {
        if (getAtom().equals(o.getAtom())) return Boolean.compare(isNegated(), o.isNegated());
        else return getAtom().compareTo(o.getAtom());
    }
}
