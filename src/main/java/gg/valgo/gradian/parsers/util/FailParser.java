package gg.valgo.gradian.parsers.util;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

/**
 * A utility parser which always fails with a given message. Useful for providing more detailed error messages in your parser.
 */
public class FailParser extends Parser<Void> {
    /**
     * The error message.
     */
    private String message;

    /**
     * Creates a new FailParser with a given message.
     * @param message The message to fail with.
     */
    public FailParser(String message) {
        this.message = message;

        setParserName("fail");
    }

    /**
     * Gets the error message.
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<Void> parse(ParserState<?> state) {
        return state.formatException(this, message).retype();
    }
}