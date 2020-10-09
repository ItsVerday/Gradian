package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

/**
 * Parses a value from a list of choices. The choices are attempted in the order they were specified, and the first one to succeed is the result. Order matters! If none of the parsers succeed, this parser will fail. This parser works with any input type.
 * @param <ResultType> The result type of this parser.
 */
public class ChoiceParser<ResultType> extends Parser<ResultType> {
    /**
     * The choices that this parser can choose from.
     */
    private Parser<ResultType>[] choices;

    /**
     * Creates a new ChoiceParser from a list of choices.
     * @param choices The list of choices.
     */
    public ChoiceParser(Parser<ResultType>... choices) {
        this.choices = choices;

        setParserName("choice");
    }

    /**
     * Gets the choices that this parser can choose from.
     * @return The choices that this parser can choose from.
     */
    public Parser<ResultType>[] getChoices() {
        return choices;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        for (Parser<ResultType> parser : choices) {
            ParserState<ResultType> resultState = parser.execute(state);

            if (!resultState.isException()) {
                return resultState;
            }
        }

        return state.formatExpectedException(this, "choice of " + choices.length + " values", state.getInput().getTruncatedString(state.getIndex())).retype();
    }

    /**
     * Returns a ChoiceParser which can handle multiple different result types from its parsers. The parser will result in a generic object, though. Use the main constructor method if possible.
     * @param parsers The list of choices.
     * @return The ChoiceParser.
     */
    public static ChoiceParser<Object> anyTypeChoice(Parser<?>... parsers) {
        Parser<Object>[] newParsers = new Parser[parsers.length];
        int index = 0;
        for (Parser<?> parser : parsers) {
            newParsers[index++] = (Parser<Object>) parser;
        }

        return new ChoiceParser<>(newParsers);
    }
}