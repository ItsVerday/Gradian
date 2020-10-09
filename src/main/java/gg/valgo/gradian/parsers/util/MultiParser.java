package gg.valgo.gradian.parsers.util;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * An abstract class representing a parser which returns a sequence of elements. This class contains utility methods for mapping the result. Extend this parser if the parser results in some sort of sequence.
 * @param <ResultType> The type of the result elements.
 */
public abstract class MultiParser<ResultType> extends Parser<ResultType[]> {
    /**
     * Utility method used to convert an array of objects to their string representations. Used in multiParser.join().
     * @param objects The objects array.
     * @return The string array.
     */
    private static String[] toString(Object[] objects) {
        String[] strings = new String[objects.length];

        int index = 0;
        for (Object object : objects) {
            strings[index++] = Objects.toString(object);
        }

        return strings;
    }

    /**
     * Maps the result of this parser to an ArrayList instead of a plain array.
     * @return The mapped parser.
     */
    public MappedParser<ResultType[], ArrayList<ResultType>> asArrayList() {
        return map(array -> new ArrayList<>(Arrays.asList(array)));
    }

    /**
     * Maps the result of this parser to one of the elements in the resulting array.
     * @param index The index of the array. If the index is negative, that value is subtracted from the array length, and that value is returned. For example, an index of -1 returns the last element of the array.
     * @return The mapped parser.
     */
    public MappedParser<ResultType[], ResultType> index(int index) {
        return map(array -> array[index >= 0 ? index : array.length - index]);
    }

    /**
     * Maps the result of this parser to return the first element of the array. Useful if the parser always returns a single-element array, and it would be easier to just have the result itself.
     * @return The mapped parser.
     */
    public MappedParser<ResultType[], ResultType> first() {
        return index(0);
    }

    /**
     * Maps the result of this parser to return the last element of the array.
     * @return The mapped parser.
     */
    public MappedParser<ResultType[], ResultType> last() {
        return index(-1);
    }

    /**
     * Maps the result of this parser to join the values (as strings) with a delimiter.
     * @param delimiter The delimiter to join strings by.
     * @return The mapped parser.
     */
    public MappedParser<ResultType[], String> join(String delimiter) {
        return map(array -> String.join(delimiter, toString(array)));
    }

    /**
     * Checks whether a parser state's result should be added to a result array. If you are adding a resulting value to an array, only do so if this method returns true when called on the parser state.
     * @param parserState The parser state to check.
     * @return Whether the result should be added to the array.
     */
    public static boolean shouldAddResult(ParserState<?> parserState) {
        return !parserState.isIgnoreResult();
    }
}