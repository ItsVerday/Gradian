package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class PeekParser extends Parser<String> {
    private int length;

    public PeekParser(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public ParserState<String> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        String substring = state.getSubstring();
        return state.updateState(state.getIndex(), substring.substring(0, Math.min(substring.length(), length)));
    }

    @Override
    public String getParserName() {
        return "peek";
    }

    @Override
    public String getExpectedValueName() {
        return "???";
    }
}