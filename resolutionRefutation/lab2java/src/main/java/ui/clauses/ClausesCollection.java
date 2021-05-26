package ui.clauses;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class ClausesCollection {
    private List<Clause> inputClauses;
    private final List<Clause> primaryInputClauses;
    private Clause goalClause;
    private Clause primaryGoalClause;

    public ClausesCollection(File listOfClausesFile) {
        requireNonNull(listOfClausesFile, "The given file containing the list of clauses cannot be null!");
        inputClauses = new ArrayList<>();
        parseListOfClauses(listOfClausesFile);
        primaryInputClauses = new ArrayList<>(inputClauses);
    }

    public List<Clause> getInputClauses() {
        return inputClauses;
    }

    public Clause getGoalClause() {
        return goalClause;
    }

    public List<Clause> getPrimaryInputClauses() {
        return primaryInputClauses;
    }

    public Clause getPrimaryGoalClause() {
        return primaryGoalClause;
    }

    public void resetClausesCollection() {
        inputClauses = new ArrayList<>(primaryInputClauses);
        goalClause = new Clause(primaryGoalClause);
    }

    public void setGoalClause(Clause goalClause) {
        this.goalClause = requireNonNull(goalClause, "The given goal clause cannot be null!");
    }

    public void setPrimaryGoalClause(Clause goalClause) {
        this.primaryGoalClause = requireNonNull(goalClause, "The given goal clause cannot be null!");
    }

    private void parseListOfClauses(File listOfClausesFile) {
        requireNonNull(listOfClausesFile, "The given file containing the list of clauses cannot be null!");

        List<String> rows = new ArrayList<>();
        String row;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(listOfClausesFile), StandardCharsets.UTF_8))) {
            while ((row = reader.readLine()) != null) {
                if (row.startsWith("#")) continue;
                rows.add(row);
            }

            for (int i = 0; i < rows.size() - 1; i++)
                inputClauses.add(Clause.parseClause(rows.get(i).strip()));

            goalClause = Clause.parseClause(rows.get(rows.size() - 1).strip());
            primaryGoalClause = new Clause(goalClause);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve clauses from the given path of the file containing the list of clauses!", e);
        }
    }
}
