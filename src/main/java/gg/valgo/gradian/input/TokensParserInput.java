package gg.valgo.gradian.input;

import java.util.List;

/**
 * A class representing a token list input to a parser.
 */
public class TokensParserInput extends ParserInput<Token<?>> {
    /**
     * The input array.
     */
    private Token<?>[] tokens;

    /**
     * Creates a new token parser input from a given token array.
     * @param tokens The token array.
     */
    public TokensParserInput(Token<?>[] tokens) {
        this.tokens = tokens;
    }

    /**
     * Creates a new token parser input from a given list of tokens.
     * @param tokens The list of tokens.
     */
    public TokensParserInput(List<Token<?>> tokens) {
        this(tokens.toArray(new Token[0]));
    }

    /**
     * Generates an array of elements that this parser input consists of. For example, a string ParserInput will generate and return an array of characters here.
     * @return The generated array of elements.
     */
    @Override
    public Token<?>[] generateElements() {
        return tokens;
    }

    /**
     * Gets the truncated string representation of the input at a specific index. Used for error messages.
     * @param index The index that the error message should start at.
     * @return The string representation.
     */
    @Override
    public String getTruncatedString(int index) {
        String truncated = "tokens ";

        int length = length();
        int finalIndex = Math.min(index + 8, length);
        for (int i = index; i < finalIndex; i++) {
            truncated = truncated.concat(tokens[index] + ", ");
        }

        if (finalIndex == length) {
            truncated = truncated.concat("*END*");
        } else {
            truncated = truncated.concat("...");
        }

        return truncated;
    }

    /**
     * Gets the name of this parser input. Used for error messages.
     * @return The name of this input.
     */
    @Override
    public String getInputName() {
        return "tokens input";
    }
}