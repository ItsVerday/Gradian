package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesInputList;
import gg.valgo.gradian.input.StringInputList;

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
        if (!(state.getInput() instanceof StringInputList) || !(state.getInput() instanceof BytesInputList)) {
            return state.badInputType(this).updateType();
        }

        if (state.isException()) {
            return state.updateType();
        }

        if (state.getInput().isEndOfInput()) {
            return state.updateState(state.getIndex(), "");
        }

        char actualCharacter;
        if (state.getInput() instanceof StringInputList) {
            actualCharacter = ((StringInputList) state.getInput()).getCurrent().charAt(0);
        } else {
            actualCharacter = (char) ((BytesInputList) state.getInput()).getCurrent().byteValue();
        }

        return state.updateState(state.getIndex(), actualCharacter + "");
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