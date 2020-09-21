package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class CharacterParser extends Parser<Character> {
    private char character;

    public CharacterParser(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public ParserState<Character> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        String substring = state.getSubstring();
        if (substring.length() == 0) {
            return state.<Character>updateType().formatException(this, "end of input");
        }

        char actualCharacter = substring.charAt(0);

        if (actualCharacter == character) {
            return state.updateState(state.getIndex() + 1, character);
        }

        return state.<Character>updateType().formatException(this, "character '" + actualCharacter + "'");
    }

    public Parser<String> asString() {
        return map(character -> Character.toString(character));
    }

    @Override
    public String getParserName() {
        return "character";
    }

    @Override
    public String getExpectedValueName() {
        return "character '" + character + "'";
    }
}