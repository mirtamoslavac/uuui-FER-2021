package ui;

import ui.algorithms.resolution.strategies.control.SetOfSupportStrategy;
import ui.clauses.*;
import ui.cooking.CookingAssistant;
import ui.commands.UserCommandCollection;
import ui.utils.OutputUtils;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import static java.util.Objects.requireNonNull;

public class Solution {
    private static final Set<String> supportedKeywords = Stream.of("resolution", "cooking").collect(Collectors.toCollection(HashSet::new));
    private static final Set<String> keywordsUsingUserCommands = Stream.of("cooking").collect(Collectors.toCollection(HashSet::new));

    public static void main(String... args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Not enough arguments given! Expected at least 2, got " + args.length + "!");
        if (args.length > 3)
            throw new IllegalArgumentException("Too many arguments given! Expected more than 3, got " + args.length + "!");

        String keyword;
        File listOfClausesFile = null, userCommandsFile = null;
        try {
            keyword = args[0].toLowerCase();
            if (!supportedKeywords.contains(keyword))
                throw new IllegalArgumentException("The given keyword is not supported! Got \"" + keyword
                        + "\", expected: " + String.join(",", supportedKeywords));

            listOfClausesFile = new File(args[1]).getAbsoluteFile();
            checkFile(listOfClausesFile, "the list of clauses");

            if (keywordsUsingUserCommands.contains(keyword)) {
                try {
                    userCommandsFile = new File(args[2]).getAbsoluteFile();
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("No user commands file given!");
                }
                checkFile(userCommandsFile, "user commands");
            }
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
            return;
        }

        try {
            ClausesCollection listOfClauses = new ClausesCollection(listOfClausesFile);
            switch (keyword) {
                case "resolution" -> {
                    SetOfSupportStrategy sosStrategy = new SetOfSupportStrategy(listOfClauses);
                    Optional<Resolvent> result = sosStrategy.algorithm();
                    if (result.isEmpty()) OutputUtils.printConclusion(listOfClauses.getGoalClause().toString());
                    else OutputUtils.printConclusion(listOfClauses.getGoalClause().toString(), result.get());
                }
                case "cooking" -> {
                    UserCommandCollection userCommands = new UserCommandCollection(userCommandsFile);
                    CookingAssistant cookingAssistant = new CookingAssistant(listOfClauses, userCommands);
                    cookingAssistant.cook();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void checkFile(File file, String output) {
        requireNonNull(file, "The given file cannot be null");
        requireNonNull(output, "The given string to be included in the output cannot be null");

        if (!file.exists())
            throw new IllegalArgumentException("The given file containing " + output + " does not exist!");
        if (!file.canRead())
            throw new IllegalArgumentException("The given file containing " + output + " cannot be read!");
    }
}
