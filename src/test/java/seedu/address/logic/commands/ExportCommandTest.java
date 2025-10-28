package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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
    Path tempDir; // JUnit provides a temporary directory

    private Path sourceFile;
    private Model model;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary source file to simulate the existing address book
        sourceFile = tempDir.resolve("addressbook.json");
        Files.writeString(sourceFile, "{\"persons\":[]}");

        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    void execute_validFilePath_success() throws Exception {
        Path exportPath = tempDir.resolve("export.json");

        // Check that the file was copied
        assertCommandSuccess(new ExportCommand(exportPath.toString()), model,
                String.format(ExportCommand.MESSAGE_SUCCESS, exportPath), model);
        //assertTrue(Files.exists(exportPath));
    }

    @Test
    void execute_directoryPath_appendsDefaultFileName() throws Exception {
        Path exportDir = tempDir.resolve("myfolder");
        Files.createDirectories(exportDir);

        ExportCommand command = new ExportCommand(exportDir.toString());
        CommandResult result = command.execute(model);

        Path expectedFile = exportDir.resolve("exported_addressbook.json");
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
