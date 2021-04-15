package ui.commands.strategies;

import ui.algorithms.resolution.strategies.control.SetOfSupportStrategy;
import ui.clauses.*;
import ui.commands.UserCommand;
import ui.utils.OutputUtils;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class QueryCommand implements ICommand {
    @Override
    public void executeCommand(UserCommand userCommand, ClausesCollection listOfClauses) {
        requireNonNull(userCommand, "The given user command cannot be null!");
        requireNonNull(listOfClauses, "The given collection of clauses cannot be null!");

        Clause toBeChecked = userCommand.getClause();
        listOfClauses.resetClausesCollection();

        if (!toBeChecked.equals(listOfClauses.getGoalClause())) {
            listOfClauses.getInputClauses().add(listOfClauses.getGoalClause());
            listOfClauses.setGoalClause(toBeChecked);
        }

        SetOfSupportStrategy sosStrategy = new SetOfSupportStrategy(listOfClauses);
        Optional<Resolvent> result = sosStrategy.algorithm();
        if (result.isEmpty()) OutputUtils.printQuery(userCommand, listOfClauses.getGoalClause().toString());
        else OutputUtils.printQuery(userCommand, listOfClauses.getGoalClause().toString(), result.get());
    }
}
