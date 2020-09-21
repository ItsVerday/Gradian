package gg.valgo.gradian.util;

public class Coroutine<ReturnType, YieldedValueType, YieldReturnType> {
    private CoroutineExecutor<ReturnType, YieldedValueType, YieldReturnType> executor;
    private YieldedValueConsumer<ReturnType, YieldedValueType, YieldReturnType> consumer;

    public Coroutine(CoroutineExecutor<ReturnType, YieldedValueType, YieldReturnType> executor, YieldedValueConsumer<ReturnType, YieldedValueType, YieldReturnType> consumer) {
        this.executor = executor;
        this.consumer = consumer;
    }

    public CoroutineResult<ReturnType> execute() {
        CoroutineContext<ReturnType, YieldedValueType, YieldReturnType> context = new CoroutineContext<>(consumer);

        try {
            CoroutineResult<ReturnType> result = new CoroutineResult<>();
            result.value = executor.execute(context);
            result.success = true;
            return result;
        } catch (CoroutineExitException e) {
            CoroutineResult<ReturnType> result = new CoroutineResult<>();
            result.value = null;
            result.success = false;
            return result;
        }
    }

    public static class CoroutineContext<ReturnType, YieldedValueType, YieldReturnType> {
        private YieldedValueConsumer<ReturnType, YieldedValueType, YieldReturnType> consumer;

        public CoroutineContext(YieldedValueConsumer<ReturnType, YieldedValueType, YieldReturnType> consumer) {
            this.consumer = consumer;
        }

        public YieldReturnType yield(YieldedValueType yieldedValue) throws CoroutineExitException {
            return consumer.consume(this, yieldedValue);
        }

        public void reject() throws CoroutineExitException {
            throw new CoroutineExitException();
        }
    }

    public static class CoroutineResult<ReturnType> {
        private ReturnType value;
        private boolean success;

        public ReturnType getValue() {
            return value;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    public interface CoroutineExecutor<ReturnType, YieldedValueType, YieldReturnType> {
        ReturnType execute(CoroutineContext<ReturnType, YieldedValueType, YieldReturnType> coroutine) throws CoroutineExitException;
    }

    public interface YieldedValueConsumer<ReturnType, YieldedValueType, YieldReturnType> {
        YieldReturnType consume(CoroutineContext<ReturnType, YieldedValueType, YieldReturnType> context, YieldedValueType yieldedValue) throws CoroutineExitException;
    }
}