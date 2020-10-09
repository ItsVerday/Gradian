package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesParserInput;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.input.StringParserInput;

/**
 * A parser which matches a single character, resulting in that character if successful. If this parser cannot match the specified character, it will fail. This parser accepts a string or byte array input.
 */
public class CharacterParser extends Parser<Character> {
    /**
     * The character to match.
     */
    private char character;

    /**
     * Creates a new character parser from a given character.
     * @param character The character to match.
     */
    public CharacterParser(char character) {
        this.character = character;
    }

    /**
     * Gets the character to match.
     * @return The character to match.
     */
    public char getCharacter() {
        return character;
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
            return state.formatExpectedException(this, "character '" + character + "'", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        Character otherCharacter = input.getElement(index);
        if (otherCharacter == character) {
            return state.updateState(index + 1, character);
        }

        return state.formatExpectedException(this, "character '" + character + "'", input.getTruncatedString(index)).retype();
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
            return state.formatExpectedException(this, "character '" + character + "'", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        Byte b = input.getElement(index);
        if ((char) b.byteValue() == character) {
            return state.updateState(index + 1, character);
        }

        return state.formatExpectedException(this, "character '" + character + "'", input.getTruncatedString(index)).retype();
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