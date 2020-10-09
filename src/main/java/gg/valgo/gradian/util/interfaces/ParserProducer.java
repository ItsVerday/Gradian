package gg.valgo.gradian.util.interfaces;

import gg.valgo.gradian.Parser;

/**
 * An interface which produces a parser.
 * @param <ResultType> The result type of the parser.
 */
public interface ParserProducer<ResultType> {
    /**
     * Produces a parser.
     * @return The produced parser.
     */
    Parser<ResultType> produce();
}