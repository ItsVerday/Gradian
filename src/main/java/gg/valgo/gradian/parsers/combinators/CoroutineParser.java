package gg.valgo.gradian.parsers.combinators;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.util.coroutine.*;

/**
 * An advanced parser which runs custom logic in a lambda. Parsers can be "yielded", and they will be parsed. If the parser succeeds, the result of the parser is returned from the yield method, and if the parser fails, the coroutine execution is stopped.
 * @param <ResultType> The result type of this parser.
 */
public class CoroutineParser<ResultType> extends Parser<ResultType> {
    /**
     * The executor, a lambda taking in a context which receives a context with yield() and reject() methods.
     */
    private CoroutineExecutor<ResultType> executor;

    /**
     * Creates a new coroutine parser from a given executor.
     * @param executor The executor, a lambda taking in a context which receives a context with yield() and reject() methods.
     */
    public CoroutineParser(CoroutineExecutor<ResultType> executor) {
        this.executor = executor;

        setParserName("coroutine");
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        CoroutineExecutionContext context = new CoroutineExecutionContext();
        context.setCurrentState(state);

        Coroutine<ResultType> coroutine = new Coroutine<>(executor, new CoroutineYieldConsumer<>() {
            @Override
            public <ReturnType> ReturnType consume(CoroutineContext<ResultType> ctx, Parser<ReturnType> value) throws CoroutineExitException {
                ParserState<ReturnType> currentState = value.execute(context.getCurrentState());
                if (currentState.isException()) {
                    context.setException(currentState);
                    ctx.reject();
                }

                context.setCurrentState(currentState);
                return currentState.getResult();
            }
        });

        CoroutineResult<ResultType> result = coroutine.execute();

        if (context.getException() != null) {
            return context.getException().setIndex(state.getIndex()).retype();
        }

        if (!result.isSuccess()) {
            return state.formatException(this, "Coroutine was rejected.").retype();
        }

        return context.getCurrentState().updateState( context.getCurrentState().getIndex(), result.getValue());
    }
}