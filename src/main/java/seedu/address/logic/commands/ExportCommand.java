package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Exports the address book data to a specified JSON file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports the current address book data to a JSON file. "
            + "Parameters: "
            + PREFIX_FILE + "FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FILE + "C:\\Users\\User\\Documents\\my_export.json";

    public static final String MESSAGE_SUCCESS = "Address book successfully exported to: %1$s";
    public static final String MESSAGE_FAILURE = "Failed to export data: %1$s";
    public static final String DEFAULT_FILE = "exported_addressbook.json";

    private final String filePath;

    /**
     * Creates an ExportCommand to export data to the specified file path.
     */
    public ExportCommand(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        try {
            Path exportPath = Paths.get(filePath);

            if (!exportPath.isAbsolute()) {
                exportPath = exportPath.toAbsolutePath();
            }

            // Ensure parent directories exist
            if (exportPath.getParent() != null) {
                Files.createDirectories(exportPath.getParent());
            }
            if (Files.exists(exportPath) && Files.isDirectory(exportPath)) {
                exportPath = exportPath.resolve(DEFAULT_FILE);
            } else if (!exportPath.toString().toLowerCase().endsWith(".json")) {
                // If it's a file but missing .json, append extension
                exportPath = Paths.get(exportPath.toString() + ".json");
            }

            // Write JSON data
            Files.copy(model.getAddressBookFilePath(), exportPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return new CommandResult(String.format(MESSAGE_SUCCESS, exportPath.toString()));

        } catch (IOException | InvalidPathException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherCommand = (ExportCommand) other;
        return filePath.equals(otherCommand.filePath);
    }
}
