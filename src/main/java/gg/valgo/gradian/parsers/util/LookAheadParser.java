package gg.valgo.gradian.parsers.util;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

/**
 * A parser which "looks ahead" by matching another parser, without consuming any input. If that parser cannot be matched, this parser will fail.
 * @param <ResultType> The result type of this parser.
 */
public class LookAheadParser<ResultType> extends Parser<ResultType> {
    /**
     * The parser to look ahead with.
     */
    private Parser<ResultType> parser;

    /**
     * Creates a new LookAhead parser from a given parser.
     * @param parser The parser to look ahead with.
     */
    public LookAheadParser(Parser<ResultType> parser) {
        this.parser = parser;

        setParserName("lookAhead");
    }

    /**
     * Gets the parser.
     * @return The parser.
     */
    public Parser<ResultType> getParser() {
        return parser;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        ParserState<ResultType> newState = parser.execute(state);
        if (newState.isException()) {
            return state.withException(newState.getException().getMessage()).retype();
        }

        return state.updateState(state.getIndex(), newState.getResult());
    }
}