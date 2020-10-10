package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.input.Token;
import gg.valgo.gradian.input.TokensParserInput;
import gg.valgo.gradian.parsers.util.MultiParser;

/**
 * A parser which matches a sequence of tokens in a token list, based only on id (data is ignored). If the sequence of tokens cannot be matched, this parser will fail. This parser only accepts a tokens input.
 */
public class TokensParser extends MultiParser<Token<?>> {
    /**
     * The tokens list.
     */
    private Token<?>[] tokens;

    /**
     * Creates a new TokensParser from a list of tokens.
     * @param tokens The tokens to match.
     */
    public TokensParser(Token<?>... tokens) {
        this.tokens = tokens;

        setParserName("tokens");
    }

    /**
     * Gets the tokens to match.
     * @return The tokens to match.
     */
    public Token<?>[] getTokens() {
        return tokens;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<Token<?>[]> parse(ParserState<?> state) {
        TokensParserInput input = (TokensParserInput) state.getInput();
        int index = state.getIndex();
        int length = tokens.length;

        Token<?>[] truncated = input.getTruncatedElements(index);
        if (truncated.length < length) {
            return state.formatExpectedException(this, formatExpected() + " (ignoring data)", input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        for (int i = 0; i < tokens.length; i++) {
            if (!tokens[i].idEquals(truncated[i])) {
                return state.formatExpectedException(this, formatExpected() + " (ignoring data)", input.getTruncatedString(index)).retype();
            }
        }

        return state.updateState(index + length, tokens);
    }

    /**
     * Checks whether a given input is valid for this parser. If not, and false is returned, the parser will be put into an errored state. If a parser works with all input types (a combinator), it should return true.
     * @param input The parser input.
     * @return Whether the parser input is valid for this parser.
     */
    @Override
    public boolean inputIsValid(ParserInput<?> input) {
        return input instanceof TokensParserInput;
    }

    /**
     * Gets the expected input type as a string, used for error messages. If a parser overrides inputIsValid(), this should also be overridden.
     * @return The name of the expected input type.
     */
    @Override
    public String getExpectedInputName() {
        return "tokens input";
    }

    /**
     * Formats the choices into a list of characters.
     * @return The formatted choices.
     */
    private String formatExpected() {
        String result = "tokens ";
        if (tokens.length == 1) {
            return "token " + tokens[0];
        } else if (tokens.length == 2) {
            return result + tokens[0] + " and " + tokens[1];
        }

        for (int i = 0; i < tokens.length - 1; i++) {
            result = result.concat(tokens[i] + ", ");
        }

        return result + "and " + tokens[tokens.length - 1] + "";
    }
}