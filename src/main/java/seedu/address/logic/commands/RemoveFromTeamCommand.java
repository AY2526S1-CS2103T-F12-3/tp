package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.util.TeamCommandUtil;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.team.Team;

/**
 * Removes a person from a team.
 */
public class RemoveFromTeamCommand extends Command {

    public static final String COMMAND_WORD = "team-remove";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a person from a team. "
            + "Parameters: INDEX(one-based positive integer)\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Person %s removed from team %s";
    public static final String MESSAGE_PERSON_NOT_IN_TEAM = "Person %s is not in team %s";
    public static final String MESSAGE_CANNOT_REMOVE_FROM_NONE = "Person %s is currently not in a team";

    private final List<Index> studentIndices;

    /**
     * Creates a RemoveFromTeamCommand to remove the specified person from the team.
     */
    public RemoveFromTeamCommand(List<Index> studentIndices) {
        requireNonNull(studentIndices);
        this.studentIndices = studentIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        StringBuilder successMessage = new StringBuilder();
        StringBuilder failureBuilder = new StringBuilder();
        boolean isInvalid = false;

        for (Index studentIndex: studentIndices) {
            Person targetPerson = TeamCommandUtil.getTargetPerson(model, studentIndex);
            String teamName = targetPerson.getTeamName();

            if (Team.isNoneTeamName(teamName)) {
                isInvalid = true;
                failureBuilder.append(String.format(MESSAGE_CANNOT_REMOVE_FROM_NONE, targetPerson.getEmail()));
            }
        }

        if (isInvalid) {
            throw new CommandException(failureBuilder.toString());
        }

        for (Index studentIndex: studentIndices) {
            Person targetPerson = TeamCommandUtil.getTargetPerson(model, studentIndex);
            String teamName = targetPerson.getTeamName();
            Team targetTeam = TeamCommandUtil.validateTeamExists(model, teamName);
            TeamCommandUtil.validatePersonMembership(targetTeam, targetPerson, MESSAGE_PERSON_NOT_IN_TEAM);

            model.removePersonFromTeam(targetPerson, targetTeam);

            Person updatedPerson = new Person(
                    targetPerson.getName(),
                    targetPerson.getPhone(),
                    targetPerson.getEmail(),
                    targetPerson.getGithub(),
                    Team.NONE);

            model.setPerson(targetPerson, updatedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            successMessage.append(String.format(MESSAGE_SUCCESS,
                    Messages.format(updatedPerson), teamName) + "\n");
        }

        return new CommandResult(successMessage.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemoveFromTeamCommand)) {
            return false;
        }

        RemoveFromTeamCommand otherRemoveFromTeamCommand = (RemoveFromTeamCommand) other;
        return studentIndices.equals(otherRemoveFromTeamCommand.studentIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", studentIndices)
                .toString();
    }
}
