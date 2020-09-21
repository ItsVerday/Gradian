package gg.valgo.gradian;

public class ParserState<ResultType> {
    private final String input;
    private int index = 0;
    private ParserException exception;

    private ResultType result = null;
    private boolean ignoreResult = false;

    public ParserState(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public String getSubstring() {
        return input.substring(index);
    }

    public int getIndex() {
        return index;
    }

    public ParserState<ResultType> setIndex(int index) {
        this.index = index;
        return this;
    }

    public ParserException getException() {
        return exception;
    }

    public ParserState<ResultType> setException(ParserException exception) {
        this.exception = exception;
        return this;
    }

    public ResultType getResult() {
        return result;
    }

    public <NewResultType> ParserState<NewResultType> setResult(NewResultType newResult) {
        ParserState<NewResultType> newState = new ParserState<>(input);
        newState.setIndex(index);
        newState.setException(exception);
        newState.result = newResult;
        return newState;
    }

    public boolean isIgnoreResult() {
        return ignoreResult;
    }

    public ParserState<ResultType> setIgnoreResult(boolean ignoreResult) {
        this.ignoreResult = ignoreResult;
        return this;
    }

    public boolean isException() {
        return getException() != null;
    }

    public ParserState<ResultType> duplicate() {
        return new ParserState<ResultType>(input).setIndex(index).setException(exception).setResult(result).setIgnoreResult(ignoreResult);
    }

    public <NewResultType> ParserState<NewResultType> updateType() {
        return duplicate().setResult(null);
    }

    public <NewResultType> ParserState<NewResultType> updateState(int index, NewResultType result) {
        return duplicate().setIndex(index).setResult(result).setIgnoreResult(false);
    }

    public ParserState<ResultType> withException(String message) {
        return duplicate().setException(new ParserException(message));
    }

    public ParserState<ResultType> formatException(Parser<?> parser, String actualValue) {
        return withException("Exception in " + parser.getParserName() + " parser (position " + index + "): Expected " + parser.getExpectedValueName() + " but got " + actualValue + " instead.");
    }

    public interface StateMapper<OldResultType, NewResultType> {
        ParserState<NewResultType> map(ParserState<OldResultType> state);
    }
}