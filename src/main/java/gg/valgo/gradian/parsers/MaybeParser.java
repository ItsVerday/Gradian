package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class MaybeParser<ResultType> extends Parser<ResultType> {
    private Parser<ResultType> parser;

    public MaybeParser(Parser<ResultType> parser) {
        this.parser = parser;
    }

    public Parser<?> getParser() {
        return parser;
    }

    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        ParserState<ResultType> tryState = parser.parse(state).updateType();
        if (tryState.isException()) {
            return state.updateState(state.getIndex(), null);
        }

        return tryState.updateType().setResult(tryState.getResult());
    }

    public Parser<ResultType> valueIfAbsent(ResultType value) {
        return map(result -> result == null ? value : result);
    }

    public Parser<ResultType> ignoreIfAbsent() {
        return mapState(state -> {
            if (state.getResult() == null) {
                return state.setIgnoreResult(true);
            }

            return state;
        });
    }

    @Override
    public String getParserName() {
        return "maybe";
    }

    @Override
    public String getExpectedValueName() {
        return "maybe " + parser.getExpectedValueName();
    }
}