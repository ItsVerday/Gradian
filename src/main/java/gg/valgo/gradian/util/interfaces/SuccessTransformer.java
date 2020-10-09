package gg.valgo.gradian.util.interfaces;

import gg.valgo.gradian.ParserState;

/**
 * A lambda which takes a result and a state, and returns a state. Used in parser.fork().
 * @param <ResultType> The result type of the parser.
 */
public interface SuccessTransformer<ResultType> {
    /**
     * Maps a result and a state to a state.
     * @param result The result.
     * @param state The state.
     * @return The transformed state.
     */
    ParserState<ResultType> transform(ResultType result, ParserState<ResultType> state);
}