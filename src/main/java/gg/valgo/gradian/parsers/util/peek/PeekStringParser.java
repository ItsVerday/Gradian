package gg.valgo.gradian.parsers.util.peek;

import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.input.StringParserInput;
import gg.valgo.gradian.parsers.util.PeekParser;

/**
 * A parser which "peeks" ahead at the upcoming string input, without consuming any input. If less characters were found than expected, the resulting string will be shortened.
 */
public class PeekStringParser extends PeekParser<Character> {
    /**
     * Creates a new PeekStringParser.
     * @param count The amount of characters to peek.
     */
    public PeekStringParser(int count) {
        super(count);

        setParserName("peekString");
    }

    /**
     * Implements the actual peeking logic, which may differ for different input types.
     * @param input The parser input.
     * @param index The parser index.
     * @return The peeked values.
     */
    @Override
    public Character[] doPeek(ParserInput<?> input, int index) {
        StringParserInput stringInput = (StringParserInput) input;
        Character[] truncated = stringInput.getTruncatedElements(index);
        Character[] peeked = new Character[Math.min(truncated.length, getAmount())];
        for (int i = 0; i < peeked.length; i++) {
            peeked[i] = truncated[i];
        }

        return peeked;
    }

    /**
     * Checks whether a given input is valid for this parser. If not, and false is returned, the parser will be put into an errored state. If a parser works with all input types (a combinator), it should return true.
     * @param input The parser input.
     * @return Whether the parser input is valid for this parser.
     */
    @Override
    public boolean inputIsValid(ParserInput<?> input) {
        return input instanceof StringParserInput;
    }

    /**
     * Gets the expected input type as a string, used for error messages. If a parser overrides inputIsValid(), this should also be overridden.
     * @return The name of the expected input type.
     */
    @Override
    public String getExpectedInputName() {
        return "string input";
    }
}
