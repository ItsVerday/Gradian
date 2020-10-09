package gg.valgo.gradian.util.coroutine;

/**
 * An interface representing a lambda for the coroutine execution.
 * @param <ReturnType> The result type of the executor.
 */
public interface CoroutineExecutor<ReturnType> {
    /**
     * Implements the logic of the coroutine. The lambda is given a single value, a context with yield() and reject() methods.
     * @param context A context with yield() and reject() methods.
     * @return The result of execution, if the method was not exited.
     * @throws CoroutineExitException Thrown if the coroutine exits.
     */
    ReturnType execute(CoroutineContext<ReturnType> context) throws CoroutineExitException;
}