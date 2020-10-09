package gg.valgo.gradian.input;
/**
 * A class representing a string input to a parser.
 */
public class StringParserInput extends ParserInput<Character> {
    /**
     * The input string.
     */
    private String string;

    /**
     * Creates a new StringParserInput from a given string.
     * @param string The input string.
     */
    public StringParserInput(String string) {
        this.string = string;
    }

    /**
     * Gets the input string.
     * @return The input string.
     */
    public String getString() {
        return string;
    }

    /**
     * Generates an array of elements that this parser input consists of. For example, a string ParserInput will generate and return an array of characters here.
     * @return The generated array of elements.
     */
    @Override
    public Character[] generateElements() {
        char[] chars = string.toCharArray();
        Character[] result = new Character[chars.length];

        int index = 0;
        for (char c : chars) {
            result[index++] = c;
        }

        return result;
    }

    /**
     * Gets the truncated string representation of the input at a specific index. Used for error messages.
     * @param index The index that the error message should start at.
     * @return The string representation.
     */
    @Override
    public String getTruncatedString(int index) {
        int endIndex = Math.min(string.length(), index + 10);
        return "string \"" + string.substring(index, endIndex) + "\"" + (endIndex < string.length() ? "..." : "");
    }

    /**
     * Gets the name of this parser input. Used for error messages.
     * @return The name of this input.
     */
    @Override
    public String getInputName() {
        return "string input";
    }

    /**
     * Gets an element at a given index in the input.
     * @param index The index of the element.
     * @return The element.
     */
    @Override
    public Character getElement(int index) {
        return string.charAt(index);
    }

    /**
     * Gets the length of this input.
     * @return The length of this input.
     */
    @Override
    public int length() {
        return string.length();
    }

    /**
     * Gets the substring of this input, starting at a given index.
     * @param index The starting index of the substring.
     * @return The substring.
     */
    public String getSubstring(int index) {
        return string.substring(index);
    }
}