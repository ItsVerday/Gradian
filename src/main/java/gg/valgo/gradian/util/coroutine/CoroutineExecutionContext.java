package gg.valgo.gradian.util.coroutine;

import gg.valgo.gradian.ParserState;

/**
 * Represents the execution context of a coroutine in a coroutine parser.
 */
public class CoroutineExecutionContext {
    /**
     * The current parserState.
     */
    private ParserState<?> currentState;

    /**
     * The exception parser state, if parsing failed.
     */
    private ParserState<?> exception = null;

    /**
     * Gets the current state.
     * @return The current state.
     */
    public ParserState<?> getCurrentState() {
        return currentState;
    }

    /**
     * Sets the current state.
     * @param currentState The current state.
     */
    public void setCurrentState(ParserState<?> currentState) {
        this.currentState = currentState;
    }

    /**
     * Gets the exception state.
     * @return The exception state.
     */
    public ParserState<?> getException() {
        return exception;
    }

    /**
     * Sets the exception state.
     * @param exception The exception state.
     */
    public void setException(ParserState<?> exception) {
        this.exception = exception;
    }
}