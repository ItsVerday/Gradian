package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class SucceedWithParser<ResultType> extends Parser<ResultType> {
    private ResultType result;

    public SucceedWithParser(ResultType result) {
        this.result = result;
    }

    public ResultType getResult() {
        return result;
    }

    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        return state.updateState(state.getIndex(), result);
    }

    @Override
    public String getParserName() {
        return "succeedWith";
    }

    @Override
    public String getExpectedValueName() {
        return "???";
    }
}