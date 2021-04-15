package ui.pairs;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class UnorderedPair<T, V> {
    final T first;
    final V second;
    
    public UnorderedPair(T first, V second) {
        this.first = requireNonNull(first, "The first element of the given pair cannot be null!");
        this.second = requireNonNull(second, "The second element of the given pair cannot be null!");
    }

    public T getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnorderedPair<?, ?> that = (UnorderedPair<?, ?>) o;
        return (first.equals(that.first) && second.equals(that.second)) || (first.equals(that.second) && second.equals(that.first));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "{" + first + ", " + second + "}";
    }
}