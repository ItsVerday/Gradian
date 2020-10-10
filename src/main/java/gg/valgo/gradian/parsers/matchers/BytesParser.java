package gg.valgo.gradian.parsers.matchers;

import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesParserInput;
import gg.valgo.gradian.parsers.util.MultiParser;

/**
 * Parses a sequence of bytes in a byte array input. If the bytes could not be found, or the remaining input isn't long enough, this parser will fail.
 */
public class BytesParser extends MultiParser<Byte> {
    /**
     * The byte array to match.
     */
    private byte[] bytes;

    /**
     * The autoboxxed bytes, returned in parse.
     */
    private Byte[] autoboxxed;

    /**
     * Creates a new BytesParser from a given byte array.
     * @param bytes The byte array.
     */
    public BytesParser(byte... bytes) {
        this.bytes = bytes;
        autoboxxed = new Byte[bytes.length];

        int i = 0;
        for (byte b : bytes) {
            autoboxxed[i++] = b;
        }

        setParserName("bytes");
    }

    /**
     * Gets the bytes to match.
     * @return The bytes to match.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<Byte[]> parse(ParserState<?> state) {
        BytesParserInput input = (BytesParserInput) state.getInput();
        int index = state.getIndex();
        int length = bytes.length;

        Byte[] truncated = input.getTruncatedElements(index);
        if (truncated.length < length) {
            return state.formatExpectedException(this, formatExpected(), input.getTruncatedString(index) + " (end of input reached)").retype();
        }

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != truncated[i]) {
                return state.formatExpectedException(this, formatExpected(), input.getTruncatedString(index)).retype();
            }
        }

        return state.updateState(index + length, autoboxxed);
    }

    /**
     * Formats the choices into a list of characters.
     * @return The formatted choices.
     */
    private String formatExpected() {
        String result = "bytes ";
        if (bytes.length == 1) {
            return "byte " + byteToString(bytes[0]);
        } else if (bytes.length == 2) {
            return result + byteToString(bytes[0]) + " and " + byteToString(bytes[1]);
        }

        for (int i = 0; i < bytes.length - 1; i++) {
            result = result.concat(byteToString(bytes[i]) + ", ");
        }

        return result + "and " + byteToString(bytes[bytes.length - 1]);
    }

    /**
     * Converts a byte to its string representation, in the format 0xDD.
     * @param b The byte to convert.
     * @return The string representation.
     */
    private static String byteToString(byte b) {
        return "0x" + (b < 16 ? "0" : "") + Integer.toHexString(b);
    }
}