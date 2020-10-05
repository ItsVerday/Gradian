package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.util.Coroutine;

public class CoroutineParser<ResultType> extends Parser<ResultType> {
    private Coroutine.CoroutineExecutor<ResultType, Parser<?>, Object> executor;

    public CoroutineParser(Coroutine.CoroutineExecutor<ResultType, Parser<?>, Object> executor) {
        this.executor = executor;
    }

    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        Context context = new Context();
        context.setCurrentState(state);

        Coroutine<ResultType, Parser<?>, Object> coroutine = new Coroutine<>(executor, (ctx, value) -> {
            ParserState<?> currentState = value.parse(context.getCurrentState());

            if (currentState.isException()) {
                context.setException(currentState);
                ctx.reject();
            }

            context.setCurrentState(currentState);
            return currentState.getResult();
        });

        Coroutine.CoroutineResult<ResultType> result = coroutine.execute();

        if (context.getException() != null) {
            return context.getException().updateType();
        }

        if (!result.isSuccess()) {
            return state.formatException(this, "value " + state.getInput().getCurrent().toString()).updateType();
        }

        return context.getCurrentState().updateState(context.getCurrentState().getIndex(), result.getValue());
    }

    @Override
    public String getParserName() {
        return "coroutine parser";
    }

    @Override
    public String getExpectedValueName() {
        return "??? (user fail)";
    }

    public static class Context {
        private ParserState<?> currentState;
        private ParserState<?> exception = null;

        public ParserState<?> getCurrentState() {
            return currentState;
        }

        public void setCurrentState(ParserState<?> currentState) {
            this.currentState = currentState;
        }

        public ParserState<?> getException() {
            return exception;
        }

        public void setException(ParserState<?> exception) {
            this.exception = exception;
        }
    }
}