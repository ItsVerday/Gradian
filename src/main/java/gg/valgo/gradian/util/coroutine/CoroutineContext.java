package gg.valgo.gradian.util.coroutine;

import gg.valgo.gradian.Parser;

/**
 * A context passed to a Coroutine executor with a yield() and reject() method.
 * @param <ResultType> The result type of the coroutine.
 */
public class CoroutineContext<ResultType> {
    /**
     * The consumer which handles yielded values.
     */
    private CoroutineYieldConsumer<ResultType> consumer;

    /**
     * Creates a new coroutine context from a consumer.
     * @param consumer The consumer.
     */
    public CoroutineContext(CoroutineYieldConsumer<ResultType> consumer) {
        this.consumer = consumer;
    }

    /**
     * When called with a parser, this method passes the parser to the consumer for processing. If processing is successful, the result of that parser is returned.
     * @param parser The parser to yield.
     * @param <ReturnType> The return type of the parser.
     * @return The result of the parser.
     * @throws CoroutineExitException Thrown if processing is unsuccessful.
     */
    public <ReturnType> ReturnType yield(Parser<ReturnType> parser) throws CoroutineExitException {
        return consumer.consume(this, parser);
    }

    /**
     * Exits the coroutine.
     * @throws CoroutineExitException Thrown in order to exit the coroutine.
     */
    public void reject() throws CoroutineExitException {
        throw new CoroutineExitException();
    }
}