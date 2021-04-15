package ui.clauses;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.*;
import static java.lang.Integer.*;
import static java.util.Objects.requireNonNull;

public class Clause implements Comparable<Clause> {
    private final List<Literal> literals;

    public Clause(List<Literal> literals) {
        requireNonNull(literals, "The given list of literals cannot be null!");
        this.literals = literals.stream().distinct().collect(Collectors.toList());
        this.literals.sort(Comparator.naturalOrder());
    }

    public Clause(Clause clause) {
        this.literals = requireNonNull(clause, "The given clause cannot be null!").getLiterals();
    }

    public List<Literal> getLiterals() {
        return literals;
    }

    public static List<Clause> negate(Clause clause) {
        requireNonNull(clause, "The given clause to negate cannot be null!");
        return clause.getLiterals().stream()
                .map(literal -> {
                    if (literal.isNegated())
                        return new Clause(Collections.singletonList(new Literal(literal.getAtom())));
                    else
                        return new Clause(Collections.singletonList(new Literal(Literal.NEGATION_SYMBOL + literal.getAtom())));
                })
                .collect(Collectors.toList());
    }

    public static Clause parseClause(String clauseString) {
        requireNonNull(clauseString, "The given clause string cannot be null!");
        return new Clause(Arrays.stream(clauseString.toLowerCase().split(" v ")).map(Literal::new).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return literals.stream().map(Literal::toString).collect(Collectors.joining(" v "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause = (Clause) o;
        return literals.equals(clause.literals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals);
    }

    @Override
    public int compareTo(Clause o) {
        int thisSize = getLiterals().size();
        int otherSize = o.getLiterals().size();

        int minSize = min(thisSize, otherSize);
        for (int i = 0; i < minSize; i++) {
            String thisAtom = literals.get(i).getAtom();
            String oAtom = o.literals.get(i).getAtom();
            if (!thisAtom.equals(oAtom)) return thisAtom.compareTo(oAtom);
            if (literals.get(i).isNegated() != o.literals.get(i).isNegated())
                return compare(literals.get(i).isNegated(), o.literals.get(i).isNegated());
        }

        return compare(thisSize, otherSize);
    }
}
