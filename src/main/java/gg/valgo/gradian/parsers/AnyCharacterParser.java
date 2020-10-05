package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesInputList;
import gg.valgo.gradian.input.StringInputList;

public class AnyCharacterParser extends Parser<Character> {
    @Override
    public ParserState<Character> parse(ParserState<?> state) {
        if (!(state.getInput() instanceof StringInputList) || !(state.getInput() instanceof BytesInputList)) {
            return state.badInputType(this).updateType();
        }

        if (state.isException()) {
            return state.updateType();
        }

        if (state.getInput().isEndOfInput()) {
            return state.formatException(this, "end of input").updateType();
        }

        if (state.getInput() instanceof StringInputList) {
            return state.updateState(state.getIndex() + 1, ((StringInputList) state.getInput()).getCurrent().charAt(0));
        } else {
            return state.updateState(state.getIndex() + 1, (char) ((BytesInputList) state.getInput()).getCurrent().byteValue());
        }
    }

    public Parser<String> asString() {
        return map(character -> Character.toString(character));
    }

    @Override
    public String getParserName() {
        return "anyCharacter";
    }

    @Override
    public String getExpectedValueName() {
        return "any character";
    }
}