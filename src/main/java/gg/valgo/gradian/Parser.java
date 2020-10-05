package gg.valgo.gradian;

import gg.valgo.gradian.input.Token;

import java.util.*;

public abstract class Parser<ResultType> {
    private String parserName;
    public abstract ParserState<ResultType> parse(ParserState<?> state);
    public abstract String getExpectedValueName();

    /**
     * Gets the name of the parser (for error messages).
     * @return The name of the parser.
     */
    public String getParserName() {
        return parserName;
    }

    /**
     * Sets the parser name. Many parsers override this with a fixed name, so this will not always work.
     * @param parserName The new parser name.
     * @return This parser, for method chaining.
     */
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
        return parse(new ParserState<>(input));
    }

    /**
     * Runs a parser on a given byte array. The returned value is a `ParserState` with a `.getResult()` method to get the result of parsing. If the parser fails, the value of `parserState.isException()` will be true, and the `parserState.getException()` will return the exception.
     * @param bytes The byte array to parse.
     * @return A `ParserState` with information about the result, and whether the byte array could be parsed.
     */
    public ParserState<ResultType> run(byte[] bytes) {
        return parse(new ParserState<>(bytes));
    }

    /**
     * Runs a parser on a given token list. The returned value is a `ParserState` with a `.getResult()` method to get the result of parsing. If the parser fails, the value of `parserState.isException()` will be true, and the `parserState.getException()` will return the exception.
     * @param tokens The token list to parse.
     * @return A `ParserState` with information about the result, and whether the byte array could be parsed.
     */
    public ParserState<ResultType> run(ArrayList<Token> tokens) {
        return parse(new ParserState<>(tokens));
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
     * Runs a parser on a given byte array and returns the result, or throws a ParserException if the parsing fails.
     * @param bytes The byte array to parse.
     * @return The result of parsing.
     * @throws ParserException Thrown if the parser fails.
     */
    public ResultType getResult(byte[] bytes) throws ParserException {
        ParserState<ResultType> state = run(bytes);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Runs a parser on a given token list and returns the result, or throws a ParserException if the parsing fails.
     * @param tokens The token list to parse.
     * @return The result of parsing.
     * @throws ParserException Thrown if the parser fails.
     */
    public ResultType getResult(ArrayList<Token> tokens) throws ParserException {
        ParserState<ResultType> state = run(tokens);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Runs a parser, and transforms the output based on whether the parser succeeded or failed. If the parser succeeds, successTransformer is run, and if the parser fails, errorTransformer is run.
     * @param input The String to parse.
     * @param errorTransformer The error transformer, which modifies the result if an error is encountered.
     * @param successTransformer The success transformer, which modifies the result if no error is encountered.
     * @return The modified parser state.
     */
    public ParserState<ResultType> fork(String input, ErrorTransformer<ResultType> errorTransformer, SuccessTransformer<ResultType> successTransformer) {
        ParserState<ResultType> state = run(input);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        }

        return successTransformer.transform(state.getResult(), state);
    }

    /**
     * Runs a parser, and transforms the output based on whether the parser succeeded or failed. If the parser succeeds, successTransformer is run, and if the parser fails, errorTransformer is run.
     * @param bytes The byte array to parse.
     * @param errorTransformer The error transformer, which modifies the result if an error is encountered.
     * @param successTransformer The success transformer, which modifies the result if no error is encountered.
     * @return The modified parser state.
     */
    public ParserState<ResultType> fork(byte[] bytes, ErrorTransformer<ResultType> errorTransformer, SuccessTransformer<ResultType> successTransformer) {
        ParserState<ResultType> state = run(bytes);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        }

        return successTransformer.transform(state.getResult(), state);
    }

    /**
     * Runs a parser, and transforms the output based on whether the parser succeeded or failed. If the parser succeeds, successTransformer is run, and if the parser fails, errorTransformer is run.
     * @param tokens The token list to parse.
     * @param errorTransformer The error transformer, which modifies the result if an error is encountered.
     * @param successTransformer The success transformer, which modifies the result if no error is encountered.
     * @return The modified parser state.
     */
    public ParserState<ResultType> fork(ArrayList<Token> tokens, ErrorTransformer<ResultType> errorTransformer, SuccessTransformer<ResultType> successTransformer) {
        ParserState<ResultType> state = run(tokens);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        }

        return successTransformer.transform(state.getResult(), state);
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

    public interface SuccessTransformer<ResultType> {
        ParserState<ResultType> transform(ResultType object, ParserState<ResultType> state);
    }

    public interface ErrorTransformer<ResultType> {
        ParserState<ResultType> transform(String message, ParserState<ResultType> state);
    }
}