package ui.commands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class UserCommandCollection {
    private final List<UserCommand> userCommands;

    public UserCommandCollection(File userCommandsFile) {
        requireNonNull(userCommandsFile, "The given file containing user commands cannot be null!");
        userCommands = new ArrayList<>();
        parseUserCommands(userCommandsFile);
    }

    public List<UserCommand> getUserCommands() {
        return userCommands;
    }

    private void parseUserCommands(File userCommandsFile) {
        requireNonNull(userCommandsFile, "The given file containing user commands cannot be null!");

        String row;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(userCommandsFile), StandardCharsets.UTF_8))) {
            while ((row = reader.readLine()) != null) {
                if (row.startsWith("#")) continue;
                userCommands.add(new UserCommand(row.strip()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve user commands from the given path of the file containing user commands!", e);
        }
    }
}