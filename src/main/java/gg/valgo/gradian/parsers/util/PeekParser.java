package gg.valgo.gradian.parsers.util;

import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.ParserInput;

/**
 * A parser which "peeks" ahead at the upcoming elements in the input, without consuming any input. If less elements were found than expected, the resulting value will be shortened.
 * @param <ResultType> The result type of this parser.
 */
public abstract class PeekParser<ResultType> extends MultiParser<ResultType> {
    /**
     * The amount of elements to peek.
     */
    private int amount;

    /**
     * Creates a new PeekParser from a given element count.
     * @param amount The amount of elements to peek.
     */
    public PeekParser(int amount) {
        this.amount = amount;

        setParserName("peek");
    }

    /**
     * Gets the amount of elements that the parser will peek.
     * @return The amount of elements the parser will peek.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Implements the actual peeking logic, which may differ for different input types.
     * @param input The parser input.
     * @param index The parser index.
     * @return The peeked values.
     */
    public abstract ResultType[] doPeek(ParserInput<?> input, int index);

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType[]> parse(ParserState<?> state) {
        ParserInput<?> input = state.getInput();
        int index = state.getIndex();

        return state.updateState(index, doPeek(input, index));
    }
}