package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.parsers.util.MultiParser;

import java.util.ArrayList;

/**
 * Parses a sequence of parsers in order, and returns the results of each parser in order. If any of the parsers in the sequence fail, this parser will fail  This parser works with any input type.
 * @param <ResultType> The result type of this parser.
 */
public class SequenceParser<ResultType> extends MultiParser<ResultType> {
    /**
     * The sequence of parsers.
     */
    private Parser<ResultType>[] values;

    /**
     * Creates a new SequenceParser from a given list of parsers.
     * @param values The parser sequence.
     */
    public SequenceParser(Parser<ResultType>... values) {
        this.values = values;
    }

    /**
     * Gets the sequence of parsers.
     * @return The parser sequence.
     */
    public Parser<ResultType>[] getValues() {
        return values;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType[]> parse(ParserState<?> state) {
        ArrayList<ResultType> results = new ArrayList<>();

        for (Parser<ResultType> parser : values) {
            ParserState<ResultType> newState = parser.execute(state);

            if (newState.isException()) {
                return state.withException(newState.getException().getMessage()).retype();
            }

            if (shouldAddResult(newState)) {
                results.add(newState.getResult());
            }

            state = newState;
        }

        return state.updateState(state.getIndex(), (ResultType[]) results.toArray());
    }

    /**
     * Returns a SequenceParser which can handle multiple different result types from its parsers. The parser will result in a generic object, though. Use the main constructor method if possible.
     * @param parsers The parser sequence.
     * @return The SequenceParser.
     */
    public static SequenceParser<Object> anyTypeSequence(Parser<?>... parsers) {
        Parser<Object>[] newParsers = new Parser[parsers.length];
        int index = 0;
        for (Parser<?> parser : parsers) {
            newParsers[index++] = (Parser<Object>) parser;
        }

        return new SequenceParser<>(newParsers);
    }
}