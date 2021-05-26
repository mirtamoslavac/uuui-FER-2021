package ui.commands.strategies;

import ui.clauses.ClausesCollection;
import ui.commands.UserCommand;

public interface ICommand {
    void executeCommand(UserCommand userCommand, ClausesCollection listOfClauses);
}
