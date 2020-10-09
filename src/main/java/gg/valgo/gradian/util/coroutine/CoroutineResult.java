package gg.valgo.gradian.util.coroutine;

/**
 * The result of a coroutine.
 * @param <ResultType> The result type of the coroutine.
 */
public class CoroutineResult<ResultType> {
    /**
     * The result of coroutine execution.
     */
    private ResultType value;

    /**
     * Whether the coroutine succeeded.
     */
    private boolean success;

    /**
     * Gets the result value.
     * @return The result value.
     */
    public ResultType getValue() {
        return value;
    }

    /**
     * Sets the result value.
     * @param value The result value.
     */
    public void setValue(ResultType value) {
        this.value = value;
    }

    /**
     * Gets whether the coroutine succeeded.
     * @return Whether the coroutine succeeded.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets whether the coroutine succeeded.
     * @param success Whether the coroutine succeeded.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}