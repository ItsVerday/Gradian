package gg.valgo.gradian;

import gg.valgo.gradian.input.ParserInput;

/**
 * A class keeping track of the current state of parsing. It keeps track of the input, the current parsing index, the result (and whether it should be ignored), and the parsing exception if parsing failed.
 * @param <ResultType> The result type of this ParserState.
 */
public class ParserState<ResultType> {
    /**
     * The input to the parser.
     */
    private ParserInput<?> input;

    /**
     * The current index of the parser in the input.
     */
    private int index = 0;

    /**
     * A parser exception, if the parser has failed.
     */
    private ParserException exception = null;

    /**
     * The result of parsing.
     */
    private ResultType result = null;

    /**
     * Whether the parsing result should be ignored.
     */
    private boolean ignoreResult = false;

    /**
     * Creates a new ParserState from an input.
     * @param input The input.
     */
    public ParserState(ParserInput<?> input) {
        this.input = input;
    }

    /**
     * Gets the parser input.
     * @return The parser input.
     */
    public ParserInput<?> getInput() {
        return input;
    }

    /**
     * Sets the parser input.
     * @param input The new parser input.
     * @return This ParserState, for method chaining.
     */
    public ParserState<ResultType> setInput(ParserInput<?> input) {
        this.input = input;
        return this;
    }

    /**
     * Gets the current parser index.
     * @return The current parser index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the current parser index.
     * @param index The new parser index.
     * @return This ParserState, for method chaining.
     */
    public ParserState<ResultType> setIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Gets whether the parser had an exception (whether it failed).
     * @return Whether the parser had an exception.
     */
    public boolean isException() {
        return exception != null;
    }

    /**
     * Gets the parser exception, if it exists.
     * @return The parser exception, or null if there was no exception.
     */
    public ParserException getException() {
        return exception;
    }

    /**
     * Sets the parser exception.
     * @param exception The new parser exception, or null if the exception is to be cleared.
     * @return This ParserState, for method chaining.
     */
    public ParserState<ResultType> setException(ParserException exception) {
        this.exception = exception;
        return this;
    }

    /**
     * Gets the parser result.
     * @return The result of parsing.
     */
    public ResultType getResult() {
        return result;
    }

    /**
     * Sets the parser result.
     * @param result The new result of parsing.
     * @return This ParserState, for method chaining.
     */
    public ParserState<ResultType> setResult(ResultType result) {
        this.result = result;
        return this;
    }

    /**
     * Gets whether the result should be ignored (eg. in a list result).
     * @return Whether the result should be ignored.
     */
    public boolean isIgnoreResult() {
        return ignoreResult;
    }

    /**
     * Sets whether the result should be ignored.
     * @param ignoreResult Whether the result should be ignored.
     * @return This ParserState, for method chaining.
     */
    public ParserState<ResultType> setIgnoreResult(boolean ignoreResult) {
        this.ignoreResult = ignoreResult;
        return this;
    }

    /**
     * Duplicates this ParserState, and returns the duplicate.
     * @return The duplicate ParserState.
     */
    public ParserState<ResultType> duplicate() {
        return new ParserState<ResultType>(input).setIndex(index).setException(exception).setResult(result).setIgnoreResult(ignoreResult);
    }

    /**
     * Converts the result type of this ParserState, and returns the new ParserState.
     * @param <NewResultType> The new result type.
     * @return The retyped ParserState.
     */
    public <NewResultType> ParserState<NewResultType> retype() {
        return new ParserState<NewResultType>(input).setIndex(index).setException(exception).setResult(null).setIgnoreResult(ignoreResult);
    }

    /**
     * Updates the ParserState with a new index and result.
     * @param index The new index.
     * @param result The new result.
     * @param <NewResultType> The type of the new result.
     * @return The updated ParserState.
     */
    public <NewResultType> ParserState<NewResultType> updateState(int index, NewResultType result) {
        return this.<NewResultType>retype().setIndex(index).setResult(result).setIgnoreResult(false);
    }

    /**
     * Updates the ParserState with an exception,= with a specific message.
     * @param message The exception message.
     * @return The updated ParserState.
     */
    public ParserState<ResultType> withException(String message) {
        return duplicate().setException(new ParserException(message));
    }

    /**
     * Updates the ParserState with an exception, with a formatted message (including the parser index).
     * @param parser The parser which failed and threw this exception.
     * @param message The message of what went wrong.
     * @return The updated ParserState.
     */
    public ParserState<ResultType> formatException(Parser<?> parser, String message) {
        return withException("Exception in " + parser.getParserName() + " parser (position " + index + "): " + message);
    }

    /**
     * Updates the ParserState with an exception, with an "expected ___ but got ___ instead" message.
     * @param parser The parser which failed and threw this exception.
     * @param expected The expected value that the parser needed.
     * @param actual The actual value that the parser got instead.
     * @return The updated ParserState.
     */
    public ParserState<ResultType> formatExpectedException(Parser<?> parser, String expected, String actual) {
        return formatException(parser, "Expected " + expected + " but got " + actual + " instead.");
    }

    /**
     * Updated the ParserState with an exception, with a "bad input type" message.
     * @param parser The parser which failed and threw this exception.
     * @param expected The expected input type that the parser needed.
     * @param actual The actual input type that the parser got instead.
     * @return The updated ParserState.
     */
    public ParserState<ResultType> formatBadInputTypeException(Parser<?> parser, String expected, String actual) {
        return formatException(parser, "Bad input type! Expected " + expected + " but got " + actual + " instead.");
    }

    /**
     * Converts this parser state to a string representation.
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "ParserState {" +
                "input = " + input +
                ", index = " + index +
                ", exception = " + exception +
                ", result = " + result +
                '}';
    }
}