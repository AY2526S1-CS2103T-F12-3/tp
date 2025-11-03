package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemoveFromTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code RemoveFromTeamCommand} object
 */
public class RemoveFromTeamCommandParser implements Parser<RemoveFromTeamCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemoveFromTeamCommand}
     * and returns a {@code RemoveFromTeamCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemoveFromTeamCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        List<Index> studentIndices;

        // Extract person index from preamble (everything before the team prefix)
        try {
            studentIndices = ParserUtil.parseIndices(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(RemoveFromTeamCommand.MESSAGE_USAGE), pe);
        }



        return new RemoveFromTeamCommand(studentIndices);
    }
}
