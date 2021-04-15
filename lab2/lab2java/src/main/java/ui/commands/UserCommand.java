package ui.commands;

import ui.clauses.Clause;

import java.util.*;
import java.util.stream.*;

import static java.util.Objects.requireNonNull;

public class UserCommand {
    private static final Set<String> supportedCommands = Stream.of("?", "+", "-").collect(Collectors.toCollection(HashSet::new));
    private final Clause clause;
    private final String commandSymbol;

    public UserCommand(String commandString) {
        requireNonNull(commandString, "The given command string cannot be null!");

        clause = Clause.parseClause(commandString.substring(0, commandString.length() - 2));
        commandSymbol = commandString.substring(commandString.length() - 2).strip();
        if (!supportedCommands.contains(commandSymbol))
            throw new IllegalArgumentException("The given command symbol is not supported! Got \"" + commandSymbol
                + "\", expected: " + String.join(",", supportedCommands));
    }

    public Clause getClause() {
        return clause;
    }

    public String getCommandSymbol() {
        return commandSymbol;
    }

    @Override
    public String toString() {
        return clause.toString() + " " + commandSymbol;
    }
}
