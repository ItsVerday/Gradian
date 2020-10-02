package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class ChoiceParser extends Parser<Object> {
    private Parser<?>[] parsers;

    public ChoiceParser(Parser<?>[] parsers) {
        this.parsers = parsers;
    }

    public Parser<?>[] getParsers() {
        return parsers;
    }

    @Override
    public ParserState<Object> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        for (Parser<?> parser : parsers) {
            ParserState<?> tryState = parser.parse(state);
            if (!tryState.isException()) {
                return tryState.updateState(tryState.getIndex(), tryState.getResult());
            }
        }

        String substring = state.getSubstring();
        return state.updateType().formatException(this, "string \"" + substring.substring(0, Math.min(substring.length(), 10)) + "\"");
    }

    @Override
    public String getExpectedValueName() {
        String expectedValueName = "";
        for (Parser<?> parser : parsers) {
            expectedValueName = expectedValueName.concat(parser.getExpectedValueName() + " or ");
        }

        expectedValueName = expectedValueName.substring(0, expectedValueName.length() - 4);

        return expectedValueName;
    }
}