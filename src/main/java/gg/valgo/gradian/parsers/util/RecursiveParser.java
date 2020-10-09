package gg.valgo.gradian.parsers.util;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.util.interfaces.ParserProducer;

/**
 * A parser which accepts a lambda producing a parser, allowing for recursive parsers.
 * @param <ResultType> The result type.
 */
public class RecursiveParser<ResultType> extends Parser<ResultType> {
    /**
     * The parser producer.
     */
    private ParserProducer<ResultType> producer;

    /**
     * The parser that was produced.
     */
    private Parser<ResultType> parser;

    /**
     * Creates a new RecursiveParser from a producer.
     * @param producer The parser producer.
     */
    public RecursiveParser(ParserProducer<ResultType> producer) {
        this.producer = producer;
    }

    /**
     * Gets the parser producer.
     * @return The parser producer.
     */
    public ParserProducer<ResultType> getProducer() {
        return producer;
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        if (parser == null) {
            parser = producer.produce();
        }

        return parser.execute(state);
    }
}