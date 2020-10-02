package gg.valgo.gradian;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * A parser state, used to represent the input, current index, result, and whether there was an exception in parsing.
 * @param <ResultType> The result type of the ParserState.
 */
public class ParserState<ResultType> {
    /**
     * The input to the parser, interpreted as a UTF-8 byte array.
     */
    private final byte[] bytes;

    /**
     * Whether to update the truncated input byte array.
     */
    private boolean updateTruncatedBytesCache = true;

    /**
     * The cached truncated input byte array.
     */
    private byte[] truncatedBytesCache;

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
     * Creates a new ParserState with a specified input string.
     * @param input The input string.
     */
    public ParserState(String input) {
        bytes = input.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Creates a new ParserState with a specified input byte array.
     * @param bytes The input byte array.
     */
    public ParserState(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Gets the input bytes.
     * @return The input bytes.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Gets the input string.
     * @return The input string.
     */
    public String getInput() {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Gets the truncated bytes of this state, based off of the current index.
     * @return The truncated bytes.
     */
    public byte[] getTruncatedBytes() {
        if (updateTruncatedBytesCache) {
            truncatedBytesCache = Arrays.copyOfRange(bytes, index, bytes.length);
            updateTruncatedBytesCache = false;
        }

        return truncatedBytesCache;
    }

    /**
     * Gets the portion of the input that has not been parsed yet.
     * @return The truncated input.
     */
    public String getSubstring() {
        byte[] truncated = getTruncatedBytes();
        return new String(truncated, StandardCharsets.UTF_8);
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
        updateTruncatedBytesCache = true;

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
        ParserState<NewResultType> newState = new ParserState<>(bytes);
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
        ParserState<ResultType> duplicated = new ParserState<ResultType>(bytes).setIndex(index).setException(exception).setResult(result).setIgnoreResult(ignoreResult);
        duplicated.truncatedBytesCache = truncatedBytesCache;
        duplicated.updateTruncatedBytesCache = updateTruncatedBytesCache;

        return duplicated;
    }

    public int addIndexFromStringLength(int length) {
        String substring = getSubstring();
        String string = substring.substring(0, length);
        return index + string.getBytes(StandardCharsets.UTF_8).length;
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

    /**
     * Returns a string representation of this parser state.
     * @return The representation of this parser state.
     */
    @Override
    public String toString() {
        return  "ParserState {\n" +
                "  isException = " + isException() + "\n" +
                (isException() ? ("  exception = " + getException().getMessage() + "\n") : "") +
                (!isException() ? ("  result = " + getResult() + "\n") : "") +
                "  input = " + getInput() + "\n" +
                "  index = " + getIndex() + "\n" +
                "}";
    }

    /**
     * Runs System.out.println() on this parser state, logging it to the console. Useful for debugging.
     */
    public void debug() {
        System.out.println(this);
    }

    public interface StateMapper<OldResultType, NewResultType> {
        ParserState<NewResultType> map(ParserState<OldResultType> state);
    }
}