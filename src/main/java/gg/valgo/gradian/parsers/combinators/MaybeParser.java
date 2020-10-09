package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.parsers.util.MappedParser;

/**
 * Optionally parses a value, resulting in null if the value could not be matched. This parser will never fail. This parser accepts any type of input.
 * @param <ResultType> The result type of the parser.
 */
public class MaybeParser<ResultType> extends Parser<ResultType> {
    /**
     * The parser to optionally match.
     */
    private Parser<ResultType> optionallyMatch;

    /**
     * Creates a MaybeParser from a given parser to optionally match.
     * @param optionallyMatch The parser to optionally match.
     */
    public MaybeParser(Parser<ResultType> optionallyMatch) {
        this.optionallyMatch = optionallyMatch;

        setParserName("maybe");
    }

    /**
     * Gets the parser to optionally match.
     * @return The parser to optionally match.
     */
    public Parser<ResultType> getOptionallyMatch() {
        return optionallyMatch;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        ParserState<ResultType> newState = optionallyMatch.execute(state);

        if (newState.isException()) {
            return state.updateState(state.getIndex(), null);
        }

        return newState;
    }

    /**
     * Maps this parser to result in a set value if the optional parser could not be matched.
     * @param newValue The set value for if the optional parser failed.
     * @return The mapped parser.
     */
    public MappedParser<ResultType, ResultType> valueIfAbsent(ResultType newValue) {
        return map(value -> value == null ? newValue : value);
    }

    /**
     * Maps this parser to ignore its result if the optional parser fails.
     * @return The mapped parser.
     */
    public MappedParser<ResultType, ResultType> ignoreIfAbsent() {
        return mapState(state -> state.getResult() == null ? state.setIgnoreResult(true) : state);
    }
}