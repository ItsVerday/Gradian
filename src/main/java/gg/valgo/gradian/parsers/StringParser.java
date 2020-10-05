package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesInputList;
import gg.valgo.gradian.input.StringInputList;

import java.nio.charset.StandardCharsets;

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
        if (!(state.getInput() instanceof StringInputList) || !(state.getInput() instanceof BytesInputList)) {
            return state.badInputType(this).updateType();
        }

        if (state.isException()) {
            return state.updateType();
        }

        String substring;
        if (state.getInput() instanceof StringInputList) {
            StringInputList input = (StringInputList) state.getInput();
            substring = input.getSubstring();
        } else {
            BytesInputList inputList = (BytesInputList) state.getInput();
            Byte[] bytes = (Byte[]) inputList.getTruncated().toArray();
            byte[] b = new byte[bytes.length];
            int index = 0;
            for (Byte b2 : bytes) {
                b[index++] = b2;
            }

            substring = new String(b, StandardCharsets.UTF_8);
        }

        if (substring.length() == 0) {
            return state.formatException(this, "end of input").updateType();
        }

        if (substring.startsWith(string)) {
            return state.updateState(state.getIndex() + string.length(), string);
        }

        return state.formatException(this, "string \"" + substring.substring(0, Math.min(substring.length(), string.length())) + "\"").updateType();
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