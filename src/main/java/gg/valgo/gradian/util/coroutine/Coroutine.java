package gg.valgo.gradian.util.coroutine;

/**
 * A coroutine, used internally by the coroutine parser.
 * @param <ResultType> The result type of the coroutine.
 */
public class Coroutine<ResultType> {
    /**
     * The executor.
     */
    private CoroutineExecutor<ResultType> executor;

    /**
     * The yield consumer.
     */
    private CoroutineYieldConsumer<ResultType> consumer;

    /**
     * Creates a new coroutine from a given executor and yield consumer.
     * @param executor The executor.
     * @param consumer The yield consumer.
     */
    public Coroutine(CoroutineExecutor<ResultType> executor, CoroutineYieldConsumer<ResultType> consumer) {
        this.executor = executor;
        this.consumer = consumer;
    }

    /**
     * Runs this coroutine.
     * @return A coroutine result, with the result of the coroutine and whether execution was successful.
     */
    public CoroutineResult<ResultType> execute() {
        CoroutineContext<ResultType> context = new CoroutineContext<>(consumer);

        try {
            CoroutineResult<ResultType> result = new CoroutineResult<>();
            result.setValue(executor.execute(context));
            result.setSuccess(true);
            return result;
        } catch (CoroutineExitException e) {
            CoroutineResult<ResultType> result = new CoroutineResult<>();
            result.setValue(null);
            result.setSuccess(false);
            return result;
        }
    }
}