package gg.valgo.gradian;

import gg.valgo.gradian.util.interfaces.ErrorTransformer;
import gg.valgo.gradian.util.interfaces.ParserResultMapper;
import gg.valgo.gradian.util.interfaces.ParserStateMapper;
import gg.valgo.gradian.util.interfaces.SuccessTransformer;
import gg.valgo.gradian.parsers.util.MappedParser;
import gg.valgo.gradian.input.*;

import java.util.List;
import java.util.Objects;

/**
 * Represents a parser which parses an input. It updates the state with a new index and result, or if the parsing failed, returns a state with an exception.
 * @param <ResultType> The type of the result of this parser.
 */
public abstract class Parser<ResultType> {
    /**
     * The name of this parser, used in error messages.
     */
    private String parserName;

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    public abstract ParserState<ResultType> parse(ParserState<?> state);

    /**
     * Gets the name of this parser, used in error messages.
     * @return The name of this parser.
     */
    public String getParserName() {
        return parserName;
    }

    /**
     * Sets the name of this parser, used in error messages.
     * @param parserName The name of this parser.
     * @return This parser, for method chaining.
     */
    public Parser<ResultType> setParserName(String parserName) {
        this.parserName = parserName;
        return this;
    }

    /**
     * Checks whether a given input is valid for this parser. If not, and false is returned, the parser will be put into an errored state. If a parser works with all input types (a combinator), it should return true.
     * @param input The parser input.
     * @return Whether the parser input is valid for this parser.
     */
    public boolean inputIsValid(ParserInput<?> input) {
        return true;
    }

    /**
     * Gets the expected input type as a string, used for error messages. If a parser overrides inputIsValid(), this should also be overridden.
     * @return The name of the expected input type.
     */
    public String getExpectedInputName() {
        return "any input";
    }

    /**
     * Runs a parser on a certain state, validating the state before running the parsing logic.
     * @return The updated parser state.
     */
    public ParserState<ResultType> execute(ParserState<?> state) {
        if (state.isException()) {
            return state.retype();
        }

        if (!inputIsValid(state.getInput())) {
            return state.formatBadInputTypeException(this, getExpectedInputName(), state.getInput().getInputName()).retype();
        }

        return parse(state);
    }

    /**
     * Runs a parser on a given input string, returning the resulting state. Running state.getResult() will return the result (if parsing was successful). If parsing failed, state.isException() will return true, and state.getException() will return the exception.
     * @param input The string input to this parser.
     * @return The resulting parser state.
     */
    public ParserState<ResultType> run(String input) {
        return execute(new ParserState<>(new StringParserInput(input)));
    }

    /**
     * Runs a parser on a given input byte array, returning the resulting state. Running state.getResult() will return the result (if parsing was successful). If parsing failed, state.isException() will return true, and state.getException() will return the exception.
     * @param input The byte array input to this parser.
     * @return The resulting parser state.
     */
    public ParserState<ResultType> run(byte[] input) {
        return execute(new ParserState<>(new BytesParserInput(input)));
    }

    /**
     * Runs a parser on a given input token array, returning the resulting state. Running state.getResult() will return the result (if parsing was successful). If parsing failed, state.isException() will return true, and state.getException() will return the exception.
     * @param input The token array input to this parser.
     * @return The resulting parser state.
     */
    public ParserState<ResultType> run(Token<?>[] input) {
        return execute(new ParserState<>(new TokensParserInput(input)));
    }

    /**
     * Runs a parser on a given input token array, returning the resulting state. Running state.getResult() will return the result (if parsing was successful). If parsing failed, state.isException() will return true, and state.getException() will return the exception.
     * @param input The token array input to this parser.
     * @return The resulting parser state.
     */
    public ParserState<ResultType> run(List<Token<?>> input) {
        return execute(new ParserState<>(new TokensParserInput(input)));
    }

    /**
     * Runs a parser on a given input, returning the resulting state. Running state.getResult() will return the result (if parsing was successful). If parsing failed, state.isException() will return true, and state.getException() will return the exception.
     * @param input The input to this parser.
     * @return The resulting parser state.
     */
    public ParserState<ResultType> run(ParserInput<?> input) {
        return execute(new ParserState<>(input));
    }

    /**
     * Runs a parser on a given input string. If parsing is successful, the result is immediately returned. If parsing fails, a ParserException is thrown.
     * @param input The string input to this parser.
     * @return The result of parsing.
     * @throws ParserException Thrown if parsing fails.
     */
    public ResultType getResult(String input) throws ParserException {
        ParserState<ResultType> state = run(input);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Runs a parser on a given input byte array. If parsing is successful, the result is immediately returned. If parsing fails, a ParserException is thrown.
     * @param input The byte array input to this parser.
     * @return The result of parsing.
     * @throws ParserException Thrown if parsing fails.
     */
    public ResultType getResult(byte[] input) throws ParserException {
        ParserState<ResultType> state = run(input);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Runs a parser on a given input token array. If parsing is successful, the result is immediately returned. If parsing fails, a ParserException is thrown.
     * @param input The token array input to this parser.
     * @return The result of parsing.
     * @throws ParserException Thrown if parsing fails.
     */
    public ResultType getResult(Token<?>[] input) throws ParserException {
        ParserState<ResultType> state = run(input);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Runs a parser on a given input token list. If parsing is successful, the result is immediately returned. If parsing fails, a ParserException is thrown.
     * @param input The token list input to this parser.
     * @return The result of parsing.
     * @throws ParserException Thrown if parsing fails.
     */
    public ResultType getResult(List<Token<?>> input) throws ParserException {
        ParserState<ResultType> state = run(input);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Runs a parser on a given input. If parsing is successful, the result is immediately returned. If parsing fails, a ParserException is thrown.
     * @param input The input to this parser.
     * @return The result of parsing.
     * @throws ParserException Thrown if parsing fails.
     */
    public ResultType getResult(ParserInput<?> input) throws ParserException {
        ParserState<ResultType> state = run(input);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    /**
     * Runs a parser on a given input string, and transforms the resulting state based on whether parsing was successful or not. The transformed state is returned.
     * @param input The input string.
     * @param successTransformer The success transformer, if parsing is successful.
     * @param errorTransformer The error transformer, if parsing fails.
     * @return The transformed result.
     */
    public ParserState<ResultType> fork(String input, SuccessTransformer<ResultType> successTransformer, ErrorTransformer<ResultType> errorTransformer) {
        ParserState<ResultType> state = run(input);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        } else {
            return successTransformer.transform(state.getResult(), state);
        }
    }

    /**
     * Runs a parser on a given input byte array, and transforms the resulting state based on whether parsing was successful or not. The transformed state is returned.
     * @param input The input byte array.
     * @param successTransformer The success transformer, if parsing is successful.
     * @param errorTransformer The error transformer, if parsing fails.
     * @return The transformed result.
     */
    public ParserState<ResultType> fork(byte[] input, SuccessTransformer<ResultType> successTransformer, ErrorTransformer<ResultType> errorTransformer) {
        ParserState<ResultType> state = run(input);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        } else {
            return successTransformer.transform(state.getResult(), state);
        }
    }

    /**
     * Runs a parser on a given input token array, and transforms the resulting state based on whether parsing was successful or not. The transformed state is returned.
     * @param input The input token array.
     * @param successTransformer The success transformer, if parsing is successful.
     * @param errorTransformer The error transformer, if parsing fails.
     * @return The transformed result.
     */
    public ParserState<ResultType> fork(Token<?>[] input, SuccessTransformer<ResultType> successTransformer, ErrorTransformer<ResultType> errorTransformer) {
        ParserState<ResultType> state = run(input);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        } else {
            return successTransformer.transform(state.getResult(), state);
        }
    }

    /**
     * Runs a parser on a given input token list, and transforms the resulting state based on whether parsing was successful or not. The transformed state is returned.
     * @param input The input token list.
     * @param successTransformer The success transformer, if parsing is successful.
     * @param errorTransformer The error transformer, if parsing fails.
     * @return The transformed result.
     */
    public ParserState<ResultType> fork(List<Token<?>> input, SuccessTransformer<ResultType> successTransformer, ErrorTransformer<ResultType> errorTransformer) {
        ParserState<ResultType> state = run(input);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        } else {
            return successTransformer.transform(state.getResult(), state);
        }
    }

    /**
     * Runs a parser on a given input, and transforms the resulting state based on whether parsing was successful or not. The transformed state is returned.
     * @param input The input.
     * @param successTransformer The success transformer, if parsing is successful.
     * @param errorTransformer The error transformer, if parsing fails.
     * @return The transformed result.
     */
    public ParserState<ResultType> fork(ParserInput<?> input, SuccessTransformer<ResultType> successTransformer, ErrorTransformer<ResultType> errorTransformer) {
        ParserState<ResultType> state = run(input);

        if (state.isException()) {
            return errorTransformer.transform(state.getException().getMessage(), state);
        } else {
            return successTransformer.transform(state.getResult(), state);
        }
    }

    /**
     * Maps a result of this parser to a new result. Useful for transforming the result of a parser within the parser itself.
     * @param mapper A lambda taking in a result and returning a new result.
     * @param <NewResultType> The new result type of the parser.
     * @return The new parser, whose result will get mapped.
     */
    public <NewResultType> MappedParser<ResultType, NewResultType> map(ParserResultMapper<ResultType, NewResultType> mapper) {
        return mapState(state -> state.updateState(state.getIndex(), mapper.map(state.getResult())));
    }

    /**
     * Maps a resulting state of this parser to a new resulting state. Useful for transforming the state of a parser within the parser itself.
     * @param mapper A lambda taking in a parser state and returning a new parser state.
     * @param <NewResultType> The new result type of the parser.
     * @return The new parser, whose result state will get mapped.
     */
    public <NewResultType> MappedParser<ResultType, NewResultType> mapState(ParserStateMapper<ResultType, NewResultType> mapper) {
        return new MappedParser<>(this, mapper);
    }

    /**
     * Maps the result of this parser by casting it to a new type.
     * @param <NewResultType> The new type for the result to be casted to.
     * @return The new parser, whose result will get casted.
     */
    public <NewResultType> MappedParser<ResultType, NewResultType> castMap() {
        return map(value -> (NewResultType) value);
    }

    /**
     * Maps the result of this parser to a string, calling the toString() function.
     * @return The new parser, whose result will be stringified.
     */
    public MappedParser<ResultType, String> asString() {
        return map(Objects::toString);
    }

    /**
     * Maps the result of this parser to be ignored.
     * @return The new parser, whose result will be ignored.
     */
    public MappedParser<ResultType, ResultType> ignore() {
        return mapState(state -> state.setIgnoreResult(true).retype());
    }
}