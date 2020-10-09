package gg.valgo.gradian.util.coroutine;

import gg.valgo.gradian.Parser;

/**
 * An interface which represents a function that consumed yielded values.
 * @param <ResultType> The result type of the coroutine.
 */
public interface CoroutineYieldConsumer<ResultType> {
    /**
     * The method which "consumes" the yielded value.
     * @param context The coroutine context.
     * @param value The parser which was yielded.
     * @param <ReturnType> The result type of the parser.
     * @return The result of the parser.
     * @throws CoroutineExitException Thrown if the parser fails.
     */
    <ReturnType> ReturnType consume(CoroutineContext<ResultType> context, Parser<ReturnType> value) throws CoroutineExitException;
}