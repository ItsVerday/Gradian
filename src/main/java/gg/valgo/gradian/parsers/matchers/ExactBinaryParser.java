package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesParserInput;
import gg.valgo.gradian.input.ParserInput;

/**
 * Parses an exact binary number from a bytes input. If there are too few bytes left, or if the value couldn't be matched, this parser will fail. Otherwise, this parser results in a long.
 */
public class ExactBinaryParser extends Parser<Long> {
    /**
     * The number of bytes to parse.
     */
    private int bytes;

    /**
     * Whether the parsed value should be interpreted as signed.
     */
    private boolean signed;

    /**
     * Whether the parsed value should be interpreted as little-endian.
     */
    private boolean littleEndian;

    /**
     * The value to expect.
     */
    private long value;

    /**
     * Creates a new BinaryParser.
     * @param bytes The number of bytes to parse.
     * @param signed Whether the parsed value should be interpreted as signed.
     * @param littleEndian Whether the parsed value should be interpreted as little-endian.
     */
    public ExactBinaryParser(int bytes, boolean signed, boolean littleEndian, long value) {
        this.bytes = bytes;
        this.signed = signed;
        this.littleEndian = littleEndian;
        this.value = value;
    }

    /**
     * Gets the number of bytes to parse.
     * @return The number of bytes to parse.
     */
    public int getBytes() {
        return bytes;
    }

    /**
     * Gets whether the parsed value should be interpreted as signed.
     * @return Whether the parsed value should be interpreted as signed.
     */
    public boolean isSigned() {
        return signed;
    }

    /**
     * Gets whether the parsed value should be interpreted as little-endian.
     * @return Whether the parsed value should be interpreted as little-endian.
     */
    public boolean isLittleEndian() {
        return littleEndian;
    }

    /**
     * Gets the expected value.
     * @return The expected value.
     */
    public long getValue() {
        return value;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<Long> parse(ParserState<?> state) {
        BytesParserInput input = (BytesParserInput) state.getInput();
        Byte[] truncatedBytes = input.getTruncatedElements(state.getIndex());
        if (truncatedBytes.length < bytes) {
            return state.formatExpectedException(this, bytes + " binary bytes", truncatedBytes.length + " binary bytes").retype();
        }

        long value = 0;

        for (int index = 0; index < bytes; index++) {
            value *= 0x100;
            byte b = truncatedBytes[littleEndian ? (bytes - index - 1) : index];
            value += b < 0 ? b + 0x100 : b;
        }

        if (signed) {
            long maxValue = (long) 1 << (bytes * 8);
            value += (maxValue / 2);
            value %= maxValue;
            value -= (maxValue / 2);
        }

        if (value != this.value) {
            return state.formatExpectedException(this, "number " + this.value + " in binary", "number " + value + " in binary").retype();
        }

        return state.updateState(state.getIndex() + bytes, value);
    }

    /**
     * Maps this parser to return an int.
     * @return The mapped parser.
     */
    public Parser<Integer> asInt() {
        return castMap();
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