package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class EndOfInputParser extends Parser<Object> {
    @Override
    public ParserState<Object> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        if (state.getIndex() == state.getInput().length()) {
            return state.updateState(state.getIndex(), null);
        }

        String substring = state.getSubstring();
        return state.formatException(this, "string \"" + substring.substring(0, Math.min(substring.length(), 10)) + "\"").updateType();
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