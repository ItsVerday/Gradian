package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class EndOfInputParser extends Parser<Object> {
    @Override
    public ParserState<Object> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        if (state.getInput().isEndOfInput()) {
            return state.updateState(state.getIndex(), null);
        }

        return state.formatException(this, "more input").updateType();
    }

    @Override
    public String getParserName() {
        return "endOfInput";
    }

    @Override
    public String getExpectedValueName() {
        return "end of input";
    }
}