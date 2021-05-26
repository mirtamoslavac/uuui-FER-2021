package ui.commands.strategies;

import ui.clauses.*;
import ui.commands.UserCommand;
import ui.utils.OutputUtils;

import static java.util.Objects.requireNonNull;

public class RemovalCommand implements ICommand {
    @Override
    public void executeCommand(UserCommand userCommand, ClausesCollection listOfClauses) {
        requireNonNull(userCommand, "The given user command cannot be null!");
        requireNonNull(listOfClauses, "The given collection of clauses cannot be null!");

        Clause toBeRemoved = userCommand.getClause();
        if (!listOfClauses.getPrimaryInputClauses().contains(toBeRemoved)
                && !listOfClauses.getPrimaryGoalClause().equals(toBeRemoved)) {
            OutputUtils.printClauseRemovalFailure(toBeRemoved);
            return;
        }

        if (toBeRemoved.equals(listOfClauses.getPrimaryGoalClause())) {
            int lastIndex = listOfClauses.getPrimaryInputClauses().size() - 1;
            Clause lastClause = listOfClauses.getPrimaryInputClauses().remove(lastIndex);
            listOfClauses.setPrimaryGoalClause(lastClause);
        }

        listOfClauses.getPrimaryInputClauses().remove(toBeRemoved);
        OutputUtils.printClauseRemovalSuccess(toBeRemoved);
    }
}
