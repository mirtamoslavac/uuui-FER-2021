package ui.cooking;

import ui.clauses.*;
import ui.commands.*;
import ui.commands.strategies.*;
import ui.utils.OutputUtils;

import static java.util.Objects.requireNonNull;

public class CookingAssistant {
    private final ClausesCollection listOfClauses;
    private final UserCommandCollection userCommands;

    public CookingAssistant(ClausesCollection listOfClauses, UserCommandCollection userCommands) {
        this.listOfClauses = requireNonNull(listOfClauses, "The given collection of clauses cannot be null!");
        this.userCommands = requireNonNull(userCommands, "The given collection of user commands cannot be null!");
    }

    public void cook() {
        OutputUtils.printKnowledge(listOfClauses);

        for (UserCommand userCommand : userCommands.getUserCommands()) {
            ICommand command = switch (userCommand.getCommandSymbol()) {
                case "+" -> new AdditionCommand();
                case "-" -> new RemovalCommand();
                case "?" -> new QueryCommand();
                default -> throw new IllegalArgumentException("Unsupported command symbol \"" + userCommand.getCommandSymbol() + "\"!");
            };
            command.executeCommand(userCommand, listOfClauses);
        }
    }
}
