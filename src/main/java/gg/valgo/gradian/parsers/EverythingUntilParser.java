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
            ParserState<?> newState = parser.parse(state);

            if (!newState.isException()) {
                break;
            }

            if (state.getInput().isEndOfInput()) {
                return state.formatException(this, "... end of input").updateType();
            }

            string = string.concat(currentState.getResult().toString());
            currentState = currentState.updateState(currentState.getIndex() + 1, currentState.getResult());
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