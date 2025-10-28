package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

class ExportCommandTest {

    @TempDir
    public Path tempDir; // JUnit provides a temporary directory

    private Model model;
    private Path sourceFile;

    @BeforeEach
    void setUp() throws IOException {
        // Initialize model with typical address book
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Make sure the model's default address book file exists
        Path defaultFile = model.getAddressBookFilePath();
        Files.createDirectories(defaultFile.getParent()); // ensure parent directories
        Files.writeString(defaultFile, "{\"persons\":[]}");

    }
    @Test
    void execute_directoryPath_success() throws Exception {
        Path exportDir = tempDir.resolve("myfolder");
        Files.createDirectories(exportDir);

        ExportCommand command = new ExportCommand(exportDir.toString());
        CommandResult result = command.execute(model);

        // The command will append the default file name internally
        Path expectedFile = exportDir.resolve(ExportCommand.DEFAULT_FILE);

        // Check that the file exists
        assertTrue(Files.exists(expectedFile));

        // Compare feedback to user with the actual file path returned by the command
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, expectedFile.toString()),
                result.getFeedbackToUser());
    }

    @Test
    void execute_directoryPath_appendsDefaultFileName() throws Exception {
        Path exportDir = tempDir.resolve("myfolder");
        Files.createDirectories(exportDir);

        ExportCommand command = new ExportCommand(exportDir.toString());
        CommandResult result = command.execute(model);

        Path expectedFile = exportDir.resolve(ExportCommand.DEFAULT_FILE);
        assertTrue(Files.exists(expectedFile));
        //assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, expectedFile), result.getFeedbackToUser());
    }

    @Test
    void execute_invalidPath_throwsCommandException() {
        // Invalid path (e.g., illegal characters)
        ExportCommand command = new ExportCommand("\0invalid.json");

        assertThrows(CommandException.class, () -> command.execute(model));
    }
}
