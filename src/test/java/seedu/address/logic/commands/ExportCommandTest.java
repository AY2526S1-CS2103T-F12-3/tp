package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

class ExportCommandTest {

    @TempDir
    public Path tempDir; // JUnit provides a temporary directory

    private Model model = new ModelManager();

    @Test
    void execute_validFilePath_success() throws Exception {

        // Check that the file was copied
        assertCommandSuccess(new ExportCommand(tempDir.toString()), model,
                String.format(ExportCommand.MESSAGE_SUCCESS, tempDir.toString())
                        + "\\" + ExportCommand.DEFAULT_FILE, model);
        //assertTrue(Files.exists(exportPath));
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
