package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.ParserInput;

/**
 * A parser which matches the end of input. If the end of input has not been reached, this parser will fail. Otherwise, this parser will result in null.
 */
public class EndOfInputParser extends Parser<Void> {
    /**
     * Creates a new EndOfInputParser.
     */
    public EndOfInputParser() {
        setParserName("endOfInput");
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<Void> parse(ParserState<?> state) {
        ParserInput<?> input = state.getInput();
        int index = state.getIndex();

        if (input.isEndOfInput(index)) {
            return state.updateState(index, null);
        }

        return state.formatExpectedException(this, "end of input", input.getTruncatedString(index)).setIgnoreResult(true).retype();
    }
}