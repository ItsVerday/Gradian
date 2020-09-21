package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class StringParser extends Parser<String> {
    private String string;

    public StringParser(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public ParserState<String> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        String substring = state.getSubstring();
        if (substring.length() == 0) {
            return state.<String>updateType().formatException(this, "end of input");
        }

        if (substring.startsWith(string)) {
            return state.updateState(state.getIndex() + string.length(), string);
        }

        return state.<String>updateType().formatException(this, "string \"" + substring.substring(0, Math.min(substring.length(), string.length())) + "\"");
    }

    @Override
    public String getParserName() {
        return "string";
    }

    @Override
    public String getExpectedValueName() {
        return "string \"" + string + "\"";
    }
}