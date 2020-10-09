package gg.valgo.gradian.util.interfaces;

import gg.valgo.gradian.ParserState;

/**
 * Transforms an error message and a state into another state. Used in parser.fork().
 * @param <ResultType> The result type of the parser.
 */
public interface ErrorTransformer<ResultType> {
    /**
     * Transforms an error message and a state to a state.
     * @param message The error message.
     * @param state The state.
     * @return The transformed state.
     */
    ParserState<ResultType> transform(String message, ParserState<ResultType> state);
}