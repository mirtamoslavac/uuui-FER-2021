package ui.pairs;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class OrderedPair<T, V> extends UnorderedPair<T, V> {
    public OrderedPair(T first, V second) {
        super(requireNonNull(first, "The given first element of the ordered pair cannot be null!"),
                requireNonNull(second, "The given second element of the ordered pair cannot be null!"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedPair<?, ?> that = (OrderedPair<?, ?>) o;
        return (first.equals(that.first) && second.equals(that.second));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}