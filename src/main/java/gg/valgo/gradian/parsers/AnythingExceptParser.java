package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class AnythingExceptParser extends Parser<Character> {
    private Parser<?> parser;

    public AnythingExceptParser(Parser<?> parser) {
        this.parser = parser;
    }

    public Parser<?> getParser() {
        return parser;
    }

    @Override
    public ParserState<Character> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        ParserState<?> newState = parser.parse(state);
        if (newState.isException()) {
            return state.updateState(state.getIndex() + 1, "value " + state.getInput().getCurrent().toString()).updateType();
        }

        return state.formatException(this, parser.getExpectedValueName()).updateType();
    }

    @Override
    public String getParserName() {
        return "anythingExcept";
    }

    @Override
    public String getExpectedValueName() {
        return "anything except " + parser.getExpectedValueName();
    }
}