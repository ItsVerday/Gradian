package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.parsers.util.MultiParser;

import java.util.ArrayList;

/**
 * Parses a value separated by a separator repeatedly until it cannot parse any more of that value. If the amount of matches is not in the specified range, this parser will fail. This parser works with any input type.
 * @param <ResultType> The result type of this parser.
 */
public class SeparatedByParser<ResultType> extends MultiParser<ResultType> {
    /**
     * The separator between values, ignored in the result.
     */
    private Parser<?> separator;

    /**
     * The parser that this parser will repeat, the "values".
     */
    private Parser<ResultType> values;

    /**
     * The minimum amount of times the value should be parsed.
     */
    private int minimumCount;

    /**
     * The maximum amount of times the value should be parsed.
     */
    private int maximumCount;

    /**
     * Creates a new SeparatedByParser from a given value and separator parser, and a count range.
     * @param separator The separator between values,
     * @param values The parser to repeat.
     * @param minimumCount The minimum amount of times to parse. Use -1 for no minimum count.
     * @param maximumCount The maximum amount of times to parse. use -1 for no maximum count.
     */
    public SeparatedByParser(Parser<?> separator, Parser<ResultType> values, int minimumCount, int maximumCount) {
        this.separator = separator;
        this.values = values;
        this.minimumCount = minimumCount;
        this.maximumCount = maximumCount;

        if (minimumCount > maximumCount && minimumCount != -1 && maximumCount != -1) {
            this.minimumCount = maximumCount;
            this.maximumCount = minimumCount;
        }

        setParserName("separatedBy");
    }

    /**
     * Gets the separator parser.
     * @return The separator parser.
     */
    public Parser<?> getSeparator() {
        return separator;
    }

    /**
     * Gets the values parser.
     * @return The values parser.
     */
    public Parser<ResultType> getValues() {
        return values;
    }

    /**
     * Gets the minimum parser count.
     * @return The minimum parser count.
     */
    public int getMinimumCount() {
        return minimumCount;
    }

    /**
     * Gets the maximum parser count.
     * @return The maximum parser count.
     */
    public int getMaximumCount() {
        return maximumCount;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType[]> parse(ParserState<?> state) {
        ParserState<ResultType> currentState = state.retype();
        ArrayList<ResultType> results = new ArrayList<>();
        int parseCount = 0;

        while (!currentState.isException()) {
            int currentIndex = currentState.getIndex();
            if (parseCount > 0) {
                currentState = separator.execute(currentState).retype();
            }
            currentState = values.execute(currentState);

            if (!currentState.isException() && shouldAddResult(currentState)) {
                results.add(currentState.getResult());
            }

            if (!currentState.isException()) {
                parseCount++;
            } else {
                currentState.setIndex(currentIndex);
            }

            if (parseCount > maximumCount && maximumCount != -1) {
                break;
            }
        }

        if (parseCount < minimumCount && minimumCount != -1 || parseCount > maximumCount && maximumCount != -1) {
            return state.formatExpectedException(this, getCountRange() + " values", parseCount + " values, " + state.getInput().getTruncatedString(state.getIndex())).retype();
        }

        return currentState.setException(null).updateState(currentState.getIndex(), (ResultType[]) results.toArray());
    }

    /**
     * Utility method that formats a string with the count range this parser expects.
     * @return The formatted string.
     */
    private String getCountRange() {
        if (minimumCount == maximumCount) {
            if (minimumCount == -1) {
                return "any amount of";
            }

            return "exactly " + minimumCount;
        }

        return (minimumCount != -1 ? minimumCount + "" : "0") + "-" + (maximumCount != -1 ? maximumCount + "" : "Infinity");
    }
}