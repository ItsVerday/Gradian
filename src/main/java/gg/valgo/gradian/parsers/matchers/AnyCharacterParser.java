package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesParserInput;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.input.StringParserInput;

/**
 * A parser which matches any character, resulting in that character. If the end of input has been reached, this parser will fail. Otherwise, this parser will succeed.
 */
public class AnyCharacterParser extends Parser<Character> {
    /**
     * Creates a new AnyCharacterParser.
     */
    public AnyCharacterParser() {
        setParserName("anyCharacter");
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<Character> parse(ParserState<?> state) {
        ParserInput<?> input = state.getInput();
        if (input instanceof StringParserInput) {
            return parseStringInput(state);
        } else if (input instanceof BytesParserInput) {
            return parseBytesInput(state);
        }

        return state.formatBadInputTypeException(this, getExpectedInputName(), input.getInputName()).retype();
    }

    /**
     * Runs the parsing logic of the parser on a string input. Called internally by parse(). This method assumes that the input is a string input.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    private ParserState<Character> parseStringInput(ParserState<?> state) {
        StringParserInput input = (StringParserInput) state.getInput();
        int index = state.getIndex();

        if (input.length() < index + 1) {
            return state.formatExpectedException(this, "any character", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        char character = input.getElement(index);
        return state.updateState(index + 1, character);
    }

    /**
     * Runs the parsing logic of the parser on a byte array input. Called internally by parse(). This method assumes that the input is a byte array input.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    private ParserState<Character> parseBytesInput(ParserState<?> state) {
        BytesParserInput input = (BytesParserInput) state.getInput();
        int index = state.getIndex();

        if (input.length() < index + 1) {
            return state.formatExpectedException(this, "any character", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        byte b = input.getElement(index);
        return state.updateState(index + 1, (char) b);
    }

    /**
     * Checks whether a given input is valid for this parser. If not, and false is returned, the parser will be put into an errored state. If a parser works with all input types (a combinator), it should return true.
     * @param input The parser input.
     * @return Whether the parser input is valid for this parser.
     */
    @Override
    public boolean inputIsValid(ParserInput<?> input) {
        return input instanceof StringParserInput || input instanceof BytesParserInput;
    }

    /**
     * Gets the expected input type as a string, used for error messages. If a parser overrides inputIsValid(), this should also be overridden.
     * @return The name of the expected input type.
     */
    @Override
    public String getExpectedInputName() {
        return "string or bytes input";
    }
}