package gg.valgo.gradian.parsers.util.peek;

import gg.valgo.gradian.input.BytesParserInput;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.parsers.util.PeekParser;

/**
 * A parser which "peeks" ahead at the upcoming bytes input, without consuming any input. If less bytes were found than expected, the resulting bytes array will be shortened.
 */
public class PeekBytesParser extends PeekParser<Byte> {
    /**
     * Creates a new PeekBytesParser.
     * @param count The amount of bytes to peek.
     */
    public PeekBytesParser(int count) {
        super(count);

        setParserName("peekBytes");
    }

    /**
     * Implements the actual peeking logic, which may differ for different input types.
     * @param input The parser input.
     * @param index The parser index.
     * @return The peeked values.
     */
    @Override
    public Byte[] doPeek(ParserInput<?> input, int index) {
        BytesParserInput bytesInput = (BytesParserInput) input;
        Byte[] truncated = bytesInput.getTruncatedElements(index);
        Byte[] peeked = new Byte[Math.min(truncated.length, getAmount())];
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
        return input instanceof BytesParserInput;
    }

    /**
     * Gets the expected input type as a string, used for error messages. If a parser overrides inputIsValid(), this should also be overridden.
     * @return The name of the expected input type.
     */
    @Override
    public String getExpectedInputName() {
        return "bytes input";
    }
}