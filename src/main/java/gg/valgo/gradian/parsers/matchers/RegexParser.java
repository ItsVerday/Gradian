package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.input.StringParserInput;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser which matches a regular expression in a string. If matching was successful, the match (or a group within the match) is returned. Otherwise, the parser fails. This parser only accepts string input.
 */
public class RegexParser extends Parser<String> {
    /**
     * The pattern to match in the input.
     */
    private Pattern pattern;

    /**
     * The group to return in the match. A group of 0 (default) means the whole match will be returned.
     */
    private int group;

    /**
     * Creates a RegexParser from a given pattern and match group. If the whole match is to be returned, use group 0.
     * @param pattern The pattern to match in the input.
     * @param group The group to return in the match. Use 0 if you want the whole match.
     */
    public RegexParser(Pattern pattern, int group) {
        this.pattern = pattern;
        this.group = group;

        setParserName("regex");
    }

    /**
     * Creates a RegexParser from a given regex string, a set of expression flags, and a match group. If the whole match is to be returned, use group 0.
     * @param regex The pattern to match in the input.
     * @param flags The pattern flags for the pattern.
     * @param group The group to return in the match. Use 0 if you want the whole match.
     */
    public RegexParser(String regex, int flags, int group) {
        this(Pattern.compile(regex, flags), group);
    }

    /**
     * Creates a RegexParser from a given regex string, and a match group. The flags are defaulted to 0 (no flags). If the whole match is to be returned, use group 0.
     * @param regex The pattern to match in the input.
     * @param group The group to return in the match. Use 0 if you want the whole match.
     */
    public RegexParser(String regex, int group) {
        this(regex, 0, group);
    }

    /**
     * Creates a RegexParser from a given regex string. The flags are defaulted to 0 (no flags), and the group is defaulted to 0 (the whole match).
     * @param regex The pattern to match in the input.
     */
    public RegexParser(String regex) {
        this(regex, 0);
    }

    /**
     * Gets the pattern for this parser.
     * @return The pattern to match.
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Gets the group for this parser.
     * @return The group for this parser.
     */
    public int getGroup() {
        return group;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<String> parse(ParserState<?> state) {
        StringParserInput input = (StringParserInput) state.getInput();
        int index = state.getIndex();

        String substring = input.getSubstring(index);
        Matcher matcher = pattern.matcher(substring);
        if (!matcher.find()) {
            return state.formatExpectedException(this, "string matching pattern \"" + pattern + "\"", input.getTruncatedString(index)).retype();
        }

        String match = matcher.group(group);
        String wholeMatch = matcher.group();
        return state.updateState(index + wholeMatch.length(), match);
    }

    /**
     * Checks whether a given input is valid for this parser. If not, and false is returned, the parser will be put into an errored state. If a parser works with all input types (a combinator), it should return true.
     * @param input The parser input.
     * @return Whether the parser input is valid for this parser.
     */
    @Override
    public boolean inputIsValid(ParserInput<?> input) {
        return input instanceof StringParserInput;
    }

    /**
     * Gets the expected input type as a string, used for error messages. If a parser overrides inputIsValid(), this should also be overridden.
     * @return The name of the expected input type.
     */
    @Override
    public String getExpectedInputName() {
        return "string input";
    }
}