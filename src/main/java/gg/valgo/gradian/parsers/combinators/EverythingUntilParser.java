package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.parsers.util.MultiParser;

import java.util.ArrayList;

/**
 * Parses input elements until a certain value is reached in the input. If the end of input is reached or the input has an incorrect element type, this parser will fail. Otherwise, an array of the elements matched is returned.
 * @param <ResultType> The result type of this parser.
 */
public class EverythingUntilParser<ResultType> extends MultiParser<ResultType> {
    /**
     * The parser to parse everything up until.
     */
    private Parser<?> parser;

    /**
     * Creates a new EverythingUntilParser from a given parser.
     * @param parser The parser to parse everything up until.
     */
    public EverythingUntilParser(Parser<?> parser) {
        this.parser = parser;
    }

    /**
     * Gets the parser to parse everything up until.
     * @return The parser.
     */
    public Parser<?> getParser() {
        return parser;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType[]> parse(ParserState<?> state) {
        ArrayList<ResultType> values = new ArrayList<>();
        ParserInput<ResultType> input;

        try {
            input = (ParserInput<ResultType>) state.getInput();
        } catch (ClassCastException e) {
            return state.formatException(this, "Could not match correct input type").retype();
        }

        while (true) {
            ParserState<?> newState = parser.execute(state);

            if (newState.isException()) {
                if (input.isEndOfInput(state.getIndex())) {
                    return newState.formatExpectedException(this, "everything until a value", "end of input: " + state.getInput()).retype();
                }

                values.add(input.getElement(state.getIndex()));
                state.setIndex(state.getIndex() + 1);
            } else {
                break;
            }
        }

        return state.updateState(state.getIndex(), (ResultType[]) values.toArray());
    }
}