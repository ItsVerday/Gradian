package gg.valgo.gradian.input;

import java.util.Arrays;

/**
 * A class representing the input to a parser. Internally, this class has a list of input elements.
 * @param <ElementType> The element type of the internal list of input elements.
 */
public abstract class ParserInput<ElementType> {
    /**
     * The cache for the input elements, so that the generateElements() method only has to be called once.
     */
    private ElementType[] cache = null;

    /**
     * Generates an array of elements that this parser input consists of. For example, a string ParserInput will generate and return an array of characters here.
     * @return The generated array of elements.
     */
    public abstract ElementType[] generateElements();

    /**
     * Gets the truncated string representation of the input at a specific index. Used for error messages.
     * @param index The index that the error message should start at.
     * @return The string representation.
     */
    public abstract String getTruncatedString(int index);

    /**
     * Gets the name of this parser input. Used for error messages.
     * @return The name of this input.
     */
    public abstract String getInputName();

    /**
     * Get the elements that this parser input consists of. Always call this instead of generateElements().
     * @return The array of elements that this input consists of.
     */
    public ElementType[] getElements() {
        if (cache == null) {
            cache = generateElements();
        }

        return cache;
    }

    /**
     * Get the elements list truncated at a specific index.
     * @param index The index to truncate the list at.
     * @return The truncated array.
     */
    public ElementType[] getTruncatedElements(int index) {
        ElementType[] elements = getElements();
        return Arrays.copyOfRange(elements, index, elements.length);
    }

    /**
     * Gets an element at a given index in the input.
     * @param index The index of the element.
     * @return The element.
     */
    public ElementType getElement(int index) {
        ElementType[] elements = getElements();
        return elements[index];
    }

    /**
     * Gets the length of this input.
     * @return The length of this input.
     */
    public int length() {
        ElementType[] elements = getElements();
        return elements.length;
    }

    /**
     * Returns whether the end of input has been reached for a given index.
     * @param index The index to check.
     * @return Whether the end of input has been reached.
     */
    public boolean isEndOfInput(int index) {
        return length() <= index;
    }

    /**
     * Returns a string representation of this parser input.
     * @return The string.
     */
    @Override
    public String toString() {
        return getTruncatedString(0);
    }
}