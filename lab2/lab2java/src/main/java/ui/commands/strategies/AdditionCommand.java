package ui.commands.strategies;

import ui.clauses.*;
import ui.commands.UserCommand;
import ui.utils.OutputUtils;

import static java.util.Objects.requireNonNull;

public class AdditionCommand implements ICommand {
    @Override
    public void executeCommand(UserCommand userCommand, ClausesCollection listOfClauses) {
        requireNonNull(userCommand, "The given user command cannot be null!");
        requireNonNull(listOfClauses, "The given collection of clauses cannot be null!");

        Clause toBeAdded = userCommand.getClause();
        if (listOfClauses.getPrimaryInputClauses().contains(toBeAdded)
                || listOfClauses.getPrimaryGoalClause().equals(toBeAdded)) {
            OutputUtils.printClauseAdditionFailure(toBeAdded);
            return;
        }

        listOfClauses.getPrimaryInputClauses().add(toBeAdded);
        OutputUtils.printClauseAdditionSuccess(toBeAdded);
    }
}
