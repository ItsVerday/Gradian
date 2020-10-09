package gg.valgo.gradian.parsers.util;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

/**
 * A utility parser which always succeeds with a given value.
 * @param <ResultType> The type of the result.
 */
public class SucceedWithParser<ResultType> extends Parser<ResultType> {
    /**
     * the value to succeed with.
     */
    private ResultType value;

    /**
     * Creates a new SucceedWithParser with a given value.
     * @param value The value to succeed with.
     */
    public SucceedWithParser(ResultType value) {
        this.value = value;

        setParserName("succeedWith");
    }

    /**
     * Gets the value to succeed with.
     * @return The value to succeed with.
     */
    public ResultType getValue() {
        return value;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        return state.updateState(state.getIndex(), value);
    }
}