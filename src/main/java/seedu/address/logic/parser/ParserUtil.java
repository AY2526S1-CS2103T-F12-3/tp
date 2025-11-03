package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Github;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.team.Team;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing
     * whitespaces will be trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a string of one-based indices into a list of {@code Index} objects.
     *
     * @param oneBasedIndices a string containing space-separated one-based indices.
     * @return a list of {@code Index} objects.
     * @throws ParseException if any of the specified indices are invalid.
     */
    public static List<Index> parseIndices(String oneBasedIndices) throws ParseException {
        String[] indexStrings = oneBasedIndices.trim().split("\\s+");
        if (indexStrings.length == 0) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        List<Index> indices = new ArrayList<>();
        for (String indexString : indexStrings) {
            indices.add(parseIndex(indexString));
        }
        return indices;
    }

    /**
     * Parses a {@code String name} into a {@code Name}. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String github} into a {@code Github}. Leading and trailing whitespaces will
     * be trimmed.
     *
     * @throws ParseException if the given {@code github} is invalid.
     */
    public static Github parseGithub(String github) throws ParseException {
        requireNonNull(github);
        String trimmedGithub = github.trim();
        if (!Github.isValidGithub(trimmedGithub)) {
            throw new ParseException(Github.MESSAGE_CONSTRAINTS);
        }
        return new Github(trimmedGithub);
    }

    /**
     * Parses a {@code String email} into an {@code Email}. Leading and trailing whitespaces will be
     * trimmed. The email will be converted to lowercase to ensure case-insensitive comparison.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        String normalizedEmail = trimmedEmail.toLowerCase();
        if (!Email.isValidEmail(normalizedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(normalizedEmail);
    }

    /**
     * Parses a {@code String teamName} into a valid team name string.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code teamName} is invalid.
     */
    public static String parseTeamName(String teamName) throws ParseException {
        requireNonNull(teamName);
        String trimmedName = teamName.trim();
        if (!Team.isValidName(trimmedName)) {
            throw new ParseException(Team.MESSAGE_CONSTRAINTS);
        }
        return trimmedName;
    }
}
