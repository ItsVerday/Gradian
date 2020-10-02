package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class FailParser extends Parser<Object> {
    private String message;

    public FailParser(String message) {
        this.message = message;
    }

    @Override
    public ParserState<Object> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        return state.withException(message).updateType();
    }

    @Override
    public String getParserName() {
        return "fail";
    }

    @Override
    public String getExpectedValueName() {
        return "???";
    }
}