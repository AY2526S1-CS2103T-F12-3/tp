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
import seedu.address.model.team.exceptions.TeamMaxCapacityException;

/**
 * Adds a student (person) to a team.
 */
public class AddStudentToTeamCommand extends Command {

    public static final String COMMAND_WORD = "team-add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to a team. "
            + "Parameters: INDEX(one-based positive integer) t/TEAM_NAME\n"
            + "Example: " + COMMAND_WORD + " 1 t/Team1";

    public static final String MESSAGE_SUCCESS = "Person %s added to team %s";
    public static final String MESSAGE_TEAM_NOT_FOUND = "Team with name '%s' not found";
    public static final String MESSAGE_PERSON_ALREADY_IN_TEAM = "Person %s is already in team %s";
    public static final String MESSAGE_PERSON_IN_ANOTHER_TEAM = "Person %s is already in team %s. "
            + "Remove them from their current team before adding to a new team";
    public static final String MESSAGE_TEAM_FULL = "Team %s is at maximum capacity (5 members)";

    private final List<Index> studentIndices;
    private final String teamName;

    /**
     * Creates an AddStudentToTeamCommand to add the specified person to the team.
     */
    public AddStudentToTeamCommand(List<Index> studentIndices, String teamName) {
        requireNonNull(studentIndices);
        requireNonNull(teamName);
        this.studentIndices = studentIndices;
        this.teamName = teamName;
    }

    public List<Index> getStudentIndices() {
        return studentIndices;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Team targetTeam = TeamCommandUtil.validateTeamExists(model, teamName);

        // Disallow adding to the NONE team (sentinel)
        if (Team.isNoneTeamName(teamName) || targetTeam.equals(Team.NONE)) {
            throw new CommandException(Team.MESSAGE_CONSTRAINTS);
        }
        StringBuilder successMessage = new StringBuilder();
        for (Index studentIndex: studentIndices) {
            Person targetPerson = TeamCommandUtil.getTargetPerson(model, studentIndex);

            // Check if person is already in any team
            Team existingTeam = model.getTeamContainingPerson(targetPerson);
            if (existingTeam != null) {
                if (existingTeam.equals(targetTeam)) {
                    // Person is already in the same team
                    throw new CommandException(String.format(MESSAGE_PERSON_ALREADY_IN_TEAM,
                        Messages.format(targetPerson), teamName));
                } else {
                    // Person is in a different team
                    throw new CommandException(String.format(MESSAGE_PERSON_IN_ANOTHER_TEAM,
                        Messages.format(targetPerson), existingTeam.getName()));
                }
            }

            try {
                Person updatedPerson = new Person(targetPerson.getName(),
                        targetPerson.getPhone(),
                        targetPerson.getEmail(),
                        targetPerson.getGithub(),
                        targetTeam);

                model.addPersonToTeam(updatedPerson, targetTeam);
                model.setPerson(targetPerson, updatedPerson);
                model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                successMessage.append(String.format(MESSAGE_SUCCESS, targetPerson.getEmail(), targetTeam.getName())
                        + "\n");

            } catch (TeamMaxCapacityException e) {
                throw new CommandException(String.format(MESSAGE_TEAM_FULL, teamName));
            }
        }
        return new CommandResult(successMessage.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddStudentToTeamCommand)) {
            return false;
        }

        AddStudentToTeamCommand otherAddStudentToTeamCommand = (AddStudentToTeamCommand) other;
        return studentIndices.equals(otherAddStudentToTeamCommand.studentIndices)
                && teamName.equals(otherAddStudentToTeamCommand.teamName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentIndex", studentIndices)
                .add("teamName", teamName)
                .toString();
    }
}
