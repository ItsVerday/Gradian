package gg.valgo.gradian.input;

/**
 * A class representing a byte array input to a parser.
 */
public class BytesParserInput extends ParserInput<Byte> {
    /**
     * The input byte array.
     */
    private Byte[] bytes;

    /**
     * Creates a new BytesParserInput from a given byte array.
     * @param bytes The input byte array.
     */
    public BytesParserInput(Byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Creates a new BytesParserInput from a given byte array.
     * @param bytes The input byte array.
     */
    public BytesParserInput(byte[] bytes) {
        this(transformByteArray(bytes));
    }

    /**
     * Generates an array of elements that this parser input consists of. For example, a string ParserInput will generate and return an array of characters here.
     * @return The generated array of elements.
     */
    @Override
    public Byte[] generateElements() {
        return bytes;
    }

    /**
     * Gets the truncated string representation of the input at a specific index. Used for error messages.
     * @param index The index that the error message should start at.
     * @return The string representation.
     */
    @Override
    public String getTruncatedString(int index) {
        String truncated = "bytes ";

        int length = length();
        int finalIndex = Math.min(index + 8, length);
        for (int i = index; i < finalIndex; i++) {
            truncated = truncated.concat(byteToString(bytes[i]) + ", ");
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
        return "bytes input";
    }

    /**
     * Converts a byte to its string representation, in the format 0xDD.
     * @param b The byte to convert.
     * @return The string representation.
     */
    private static String byteToString(byte b) {
        return "0x" + (b < 16 ? "0" : "") + Integer.toHexString(b);
    }

    /**
     * Transforms a byte array into a Byte array (autoboxed).
     * @param bytes The byte array.
     * @return The resulting Byte array.
     */
    private static Byte[] transformByteArray(byte[] bytes) {
        Byte[] newBytes = new Byte[bytes.length];

        int index = 0;
        for (byte b : bytes) {
            newBytes[index++] = b;
        }

        return newBytes;
    }
}