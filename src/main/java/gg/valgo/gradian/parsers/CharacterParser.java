package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesInputList;
import gg.valgo.gradian.input.StringInputList;

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
        if (!(state.getInput() instanceof StringInputList) || !(state.getInput() instanceof BytesInputList)) {
            return state.badInputType(this).updateType();
        }

        if (state.isException()) {
            return state.updateType();
        }

        if (state.getInput().isEndOfInput()) {
            return state.formatException(this, "end of input").updateType();
        }

        char actualCharacter;
        if (state.getInput() instanceof StringInputList) {
            actualCharacter = ((StringInputList) state.getInput()).getCurrent().charAt(0);
        } else {
            actualCharacter = (char) ((BytesInputList) state.getInput()).getCurrent().byteValue();
        }

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