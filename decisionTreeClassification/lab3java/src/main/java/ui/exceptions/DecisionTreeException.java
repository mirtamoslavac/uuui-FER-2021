package ui.exceptions;

import static java.util.Objects.requireNonNull;

public class DecisionTreeException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 6135846512313246546L;

    public DecisionTreeException() {
        super();
    }

    public DecisionTreeException(String message) {
        super(requireNonNull(message));
    }
}