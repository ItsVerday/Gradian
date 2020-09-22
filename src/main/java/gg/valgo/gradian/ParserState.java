package gg.valgo.gradian;

/**
 * A parser state, used to represent the input, current index, result, and whether there was an exception in parsing.
 * @param <ResultType> The result type of the ParserState.
 */
public class ParserState<ResultType> {
    /**
     * The input to the parser.
     */
    private final String input;

    /**
     * The index in the input.
     */
    private int index = 0;

    /**
     * The exception in parsing, if it exists. If there was no exception, this will be null.
     */
    private ParserException exception;

    /**
     * The result of parsing.
     */
    private ResultType result = null;

    /**
     * Whether to ignore the result in sequencing parsers.
     */
    private boolean ignoreResult = false;

    /**
     * Creates a new ParserState with a specified input.
     * @param input The input string.
     */
    public ParserState(String input) {
        this.input = input;
    }

    /**
     * Gets the input string.
     * @return The input string.
     */
    public String getInput() {
        return input;
    }

    /**
     * Gets the portion of the input that has not been parsed yet.
     * @return The truncated input.
     */
    public String getSubstring() {
        return input.substring(index);
    }

    /**
     * Gets the current index of the parser.
     * @return The index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the ParserState.
     * @param index The index.
     * @return This ParserState, for method chaining.
     */
    public ParserState<ResultType> setIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Gets the exception of this ParserState.
     * @return The exception, or null if parsing was successful.
     */
    public ParserException getException() {
        return exception;
    }

    /**
     * Sets the exception of this ParserState.
     * @param exception The exception.
     * @return This ParserState, for method chaining.
     */
    public ParserState<ResultType> setException(ParserException exception) {
        this.exception = exception;
        return this;
    }

    /**
     * Gets the result of parsing.
     * @return The result of parsing.
     */
    public ResultType getResult() {
        return result;
    }

    /**
     * Sets the result of parsing.
     * @param newResult The new result.
     * @param <NewResultType> The type of the new result.
     * @return This ParserState, for method chaining.
     */
    public <NewResultType> ParserState<NewResultType> setResult(NewResultType newResult) {
        ParserState<NewResultType> newState = new ParserState<>(input);
        newState.setIndex(index);
        newState.setException(exception);
        newState.result = newResult;
        return newState;
    }

    /**
     * Gets whether the result should be ignored.
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
     * Gets whether there was an exception in parsing.
     * @return Whether there was an exception in parsing.
     */
    public boolean isException() {
        return getException() != null;
    }

    /**
     * Duplicates this ParserState.
     * @return A copy of this ParserState.
     */
    public ParserState<ResultType> duplicate() {
        return new ParserState<ResultType>(input).setIndex(index).setException(exception).setResult(result).setIgnoreResult(ignoreResult);
    }

    /**
     * Updates the result type of the ParserState.
     * @param <NewResultType> The new resultType.
     * @return A duplicated ParserState, with an updated result type.
     */
    public <NewResultType> ParserState<NewResultType> updateType() {
        return duplicate().setResult(null);
    }

    /**
     * Updates the state of the ParserState, used when parsing is successful.
     * @param index The new index of parsing.
     * @param result The result of parsing.
     * @param <NewResultType> The type of the result.
     * @return The duplicated ParserState, with the updated data.
     */
    public <NewResultType> ParserState<NewResultType> updateState(int index, NewResultType result) {
        return duplicate().setIndex(index).setResult(result).setIgnoreResult(false);
    }

    /**
     * Updates the exception of the ParserState with a message.
     * @param message The exception message.
     * @return The duplicated ParserState, with the updated exception.
     */
    public ParserState<ResultType> withException(String message) {
        return duplicate().setException(new ParserException(message));
    }

    /**
     * Formats an exception to be in the format "Expected *some value* but got *different value* instead."
     * @param parser The parser which threw this exception.
     * @param actualValue The value the parser got, instead of what it was expecting.
     * @return The duplicated ParserState, with the updated exception.
     */
    public ParserState<ResultType> formatException(Parser<?> parser, String actualValue) {
        return withException("Exception in " + parser.getParserName() + " parser (position " + index + "): Expected " + parser.getExpectedValueName() + " but got " + actualValue + " instead.");
    }

    public interface StateMapper<OldResultType, NewResultType> {
        ParserState<NewResultType> map(ParserState<OldResultType> state);
    }
}