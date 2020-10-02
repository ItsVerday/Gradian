package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class LookAheadParser<ResultType> extends Parser<ResultType> {
    private Parser<ResultType> parser;

    public LookAheadParser(Parser<ResultType> parser) {
        this.parser = parser;
    }

    public Parser<ResultType> getParser() {
        return parser;
    }

    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        ParserState<ResultType> newState = parser.parse(state);
        if (newState.isException()) {
            return state.withException(newState.getException().getMessage()).updateType();
        }

        return state.updateState(state.getIndex(), newState.getResult());
    }

    @Override
    public String getParserName() {
        return "lookAhead";
    }

    @Override
    public String getExpectedValueName() {
        return parser.getExpectedValueName();
    }
}