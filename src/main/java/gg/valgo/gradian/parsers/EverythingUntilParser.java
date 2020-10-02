package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class EverythingUntilParser extends Parser<String> {
    private Parser<?> parser;

    public EverythingUntilParser(Parser<?> parser) {
        this.parser = parser;
    }

    public Parser<?> getParser() {
        return parser;
    }

    @Override
    public ParserState<String> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        ParserState<?> currentState = state;
        String string = "";

        while (true) {
            String substring = currentState.getSubstring();

            ParserState<?> newState = parser.parse(state);

            if (!newState.isException()) {
                break;
            }

            if (substring.length() == 0) {
                return state.formatException(this, "... end of input").updateType();
            }

            string = string.concat(substring.substring(0, 0));
            currentState = currentState.updateState(currentState.addIndexFromStringLength(1), currentState.getResult());
        }

        return state.updateState(currentState.getIndex(), string);
    }

    @Override
    public String getParserName() {
        return "everythingUntil";
    }

    @Override
    public String getExpectedValueName() {
        return "everything until " + parser.getExpectedValueName();
    }
}