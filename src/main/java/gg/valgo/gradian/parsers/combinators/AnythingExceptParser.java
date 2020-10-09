package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.ParserInput;

/**
 * Matches anything except a certain parser. If that parser is not matched, one element (character, byte, token, ...) of input is consumed. This parser will fail if the specified parser succeeds, the end of input is reached, or the input type does not match the parser type.
 * @param <ResultType> The result type of this parser.
 */
public class AnythingExceptParser<ResultType> extends Parser<ResultType> {
    /**
     * The parser to not match.
     */
    private Parser<?> parser;

    /**
     * Creates a new AnythingExceptParser from a given parser.
     * @param parser The parser to not match.
     */
    public AnythingExceptParser(Parser<?> parser) {
        this.parser = parser;

        setParserName("anythingExcept");
    }

    /**
     * Gets the parser to not match.
     * @return The parser.
     */
    public Parser<?> getParser() {
        return parser;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        ParserState<?> newState = parser.execute(state);
        if (!newState.isException()) {
            return state.formatExpectedException(this, "anything except a value", "that value: " + state.getInput()).retype();
        }

        try {
            ParserInput<ResultType> input = (ParserInput<ResultType>) state.getInput();
            if (input.isEndOfInput(state.getIndex())) {
                return newState.formatExpectedException(this, "anything except a value", "end of input: " + state.getInput()).retype();
            }

            return state.updateState(state.getIndex() + 1, input.getElement(state.getIndex()));
        } catch (ClassCastException e) {
            return newState.formatException(this, "Could not match correct input type").retype();
        }
    }
}