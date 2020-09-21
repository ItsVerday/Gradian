package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class AnyCharacterParser extends Parser<Character> {
    @Override
    public ParserState<Character> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        String substring = state.getSubstring();
        if (substring.length() == 0) {
            return state.<Character>updateType().formatException(this, "end of input");
        }

        return state.updateState(state.getIndex() + 1, substring.charAt(0));
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