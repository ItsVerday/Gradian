package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.parsers.util.MultiParser;

import java.util.ArrayList;

/**
 * A parser which repeats another parser a specific amount of times. This parser will fail if it cannot match enough values. If there are too many values, the extras will be ignored. This parser works with any input type.
 * @param <ResultType> The result type.
 */
public class RepeatParser<ResultType> extends MultiParser<ResultType> {
    /**
     * The parser to repeat.
     */
    private Parser<ResultType> repeat;

    /**
     * The amount of times to repeat.
     */
    private int count;

    /**
     * Creates a new RepeatParser from a given parser to repeat, and a repeat count.
     * @param repeat The parser to repeat.
     * @param count The repeat count.
     */
    public RepeatParser(Parser<ResultType> repeat, int count) {
        this.repeat = repeat;
        this.count = count;

        setParserName("repeat");
    }

    /**
     * Gets the parser to repeat.
     * @return The parser to repeat.
     */
    public Parser<ResultType> getRepeat() {
        return repeat;
    }

    /**
     * Gets the amount of times to repeat.
     * @return The amount of times to repeat.
     */
    public int getCount() {
        return count;
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

        for (int index = 0; index < count; index++) {
            currentState = repeat.execute(currentState);

            if (currentState.isException()) {
                return state.formatExpectedException(this, count + " repetitions", index + " repetitions, " + state.getInput().getTruncatedString(state.getIndex())).retype();
            }

            if (shouldAddResult(currentState)) {
                results.add(currentState.getResult());
            }
        }

        return currentState.setException(null).updateState(currentState.getIndex(), (ResultType[]) results.toArray());
    }
}