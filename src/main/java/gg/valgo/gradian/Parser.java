package gg.valgo.gradian;

import java.util.*;

public abstract class Parser<ResultType> {
    private String parserName;
    public abstract ParserState<ResultType> parse(ParserState<?> state);
    public abstract String getExpectedValueName();

    public String getParserName() {
        return parserName;
    }

    public Parser<ResultType> setParserName(String parserName) {
        this.parserName = parserName;
        return this;
    }

    /**
     * Runs a parser on a given string. The returned value is a `ParserState` with a `.getResult()` method to get the result of parsing. If the parser fails, the value of `parserState.isException()` will be true, and the `parserState.getException()` will return the exception.
     * @param input The string to parse.
     * @return A `ParserState` with information about the result, and whether the string could be parsed.
     */
    public ParserState<ResultType> run(String input) {
        return parse(new ParserState<ResultType>(input));
    }

    /**
     * Runs a parser on a given string and returns the result, or throws a ParserException if the parsing fails.
     * @param input The string to parse.
     * @return The result of parsing.
     * @throws ParserException Thrown if the parser fails.
     */
    public ResultType getResult(String input) throws ParserException {
        ParserState<ResultType> state = run(input);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Maps the result of a parser to a new value. Useful for processing the result in the parser itself, instead of externally.
     * @param mapper A lambda which takes a value, the result of the parser, and returns a new value.
     * @param <NewResultType> The type of the new result.
     * @return A new parser, which results in the return value of the mapper.
     */
    public <NewResultType> Parser<NewResultType> map(final ResultMapper<ResultType, NewResultType> mapper) {
        final Parser<ResultType> self = this;

        return new Parser<>() {
            @Override
            public ParserState<NewResultType> parse(ParserState<?> state) {
                ParserState<ResultType> newState = self.parse(state);

                if (newState.isException()) {
                    return newState.updateType();
                }

                return newState.updateState(newState.getIndex(), mapper.map(newState.getResult())).setIgnoreResult(newState.isIgnoreResult());
            }

            @Override
            public String getParserName() {
                return self.getParserName();
            }

            @Override
            public String getExpectedValueName() {
                return self.getExpectedValueName();
            }
        };
    }

    /**
     * A utility method, used to cast the result of a parser to a different type. In many situations, this method will be called without the type generic, if the type can be inferred.
     * @param <NewResultType> The type to cast the result to.
     * @return A parser which results in the casted result.
     */
    public <NewResultType> Parser<NewResultType> mapType() {
        return map(value -> (NewResultType) value);
    }

    /**
     * Maps a resulting parser state to a new parser state. Can be useful for more advanced parsers, where the parser index, or the exception needs to be modified.
     * @param mapper A lambda which takes a parser state, the resulting state of the parser, and returns a new parser state.
     * @param <NewResultType> The result type of the new parser.
     * @return A new parser, which ends with a parser state mapped by the mapper.
     */
    public <NewResultType> Parser<NewResultType> mapState(ParserState.StateMapper<ResultType, NewResultType> mapper) {
        final Parser<ResultType> self = this;

        return new Parser<>() {
            @Override
            public ParserState<NewResultType> parse(ParserState<?> state) {
                ParserState<ResultType> newState = self.parse(state);

                if (newState.isException()) {
                    return newState.updateType();
                }

                return mapper.map(newState);
            }

            @Override
            public String getParserName() {
                return self.getParserName();
            }

            @Override
            public String getExpectedValueName() {
                return self.getExpectedValueName();
            }
        };
    }

    /**
     * Maps a parser to result in a string.
     * @return A parser which results in a string.
     */
    public Parser<String> asString() {
        return map(Objects::toString);
    }

    public interface ResultMapper<OldResultType, NewResultType> {
        NewResultType map(OldResultType object);
    }
}