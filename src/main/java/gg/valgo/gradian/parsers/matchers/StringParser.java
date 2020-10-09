package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesParserInput;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.input.StringParserInput;

import java.nio.charset.StandardCharsets;

/**
 * A parser which matches a string, resulting in the input string if successful. If the string cannot be matched, the parser will fail. This parser accepts a string or byte array input.
 */
public class StringParser extends Parser<String> {
    /**
     * The string to be matched.
     */
    private String string;

    /**
     * The bytes corresponding to the input string, in UTF-8. This field will be null by default, if it is needed, it will be set with generateStringBytes().
     */
    private byte[] stringBytes = null;

    /**
     * Creates a StringParser from a given string.
     * @param string The string to be matched.
     */
    public StringParser(String string) {
        this.string = string;

        setParserName("string");
    }

    /**
     * Gets the input string for this parser.
     * @return The input string.
     */
    public String getString() {
        return string;
    }

    /**
     * Gets the UTF-8 bytes of the input string for this parser. If the bytes haven't been used yet (the field is null), this method will also generate the bytes.
     * @return The input string, represented as UTF-8 bytes.
     */
    public byte[] getStringBytes() {
        generateStringBytes();
        return stringBytes;
    }

    /**
     * Generates the string bytes, in case they do not exist. This method should be called before trying to work with the byte array.
     */
    private void generateStringBytes() {
        if (stringBytes == null) {
            stringBytes = string.getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<String> parse(ParserState<?> state) {
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
    private ParserState<String> parseStringInput(ParserState<?> state) {
        StringParserInput input = (StringParserInput) state.getInput();
        int index = state.getIndex();
        int length = string.length();

        if (input.length() < index + length) {
            return state.formatExpectedException(this, "string \"" + string + "\"", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        String substring = input.getSubstring(index);
        if (substring.startsWith(string)) {
            return state.updateState(index + length, string);
        }

        return state.formatExpectedException(this, "string \"" + string + "\"", input.getTruncatedString(index)).retype();
    }

    /**
     * Runs the parsing logic of the parser on a byte array input. Called internally by parse(). This method assumes that the input is a byte array input.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    private ParserState<String> parseBytesInput(ParserState<?> state) {
        BytesParserInput input = (BytesParserInput) state.getInput();
        int index = state.getIndex();

        generateStringBytes();

        int length = stringBytes.length;
        if (input.length() < index + length) {
            return state.formatExpectedException(this, "string \"" + string + "\"", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        Byte[] bytes = input.getTruncatedElements(index);
        if (bytes.length < length) {
            return state.formatExpectedException(this, "string \"" + string + "\"", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        for (int i = 0; i < length; i++) {
            if (stringBytes[i] != bytes[i]) {
                return state.formatExpectedException(this, "string \"" + string + "\"", input.getTruncatedString(index)).retype();
            }
        }

        return state.updateState(index + length, string);
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