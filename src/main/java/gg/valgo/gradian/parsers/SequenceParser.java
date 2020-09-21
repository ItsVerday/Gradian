package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

import java.util.ArrayList;
import java.util.Arrays;

public class SequenceParser extends Parser<Object[]> {
    private Parser<?>[] parsers;

    public SequenceParser(Parser<?>[] parsers) {
        this.parsers = parsers;
    }

    public Parser<?>[] getParsers() {
        return parsers;
    }

    @Override
    public ParserState<Object[]> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        ArrayList<Object> results = new ArrayList<>();

        for (Parser<?> parser : parsers) {
            state = parser.parse(state);

            if (!state.isIgnoreResult()) {
                results.add(state.getResult());
            }
        }

        return state.updateState(state.getIndex(), results.toArray());
    }

    public Parser<ArrayList<Object>> asArrayList() {
        return map(array -> new ArrayList<>(Arrays.asList(array)));
    }

    public Parser<String> join(String delimiter) {
        return map(array -> {
            String joined = "";
            for (Object elt : array) {
                joined = joined.concat((elt != null ? elt.toString() : "null") + delimiter);
            }

            return joined.substring(0, joined.length() - delimiter.length());
        });
    }

    @Override
    public String getParserName() {
        return "sequence";
    }

    @Override
    public String getExpectedValueName() {
        String expectedValueName = "sequence of ";
        for (Parser<?> parser : parsers) {
            expectedValueName = expectedValueName.concat(parser.getExpectedValueName() + ", ");
        }

        expectedValueName = expectedValueName.substring(0, expectedValueName.length() - 2);

        return expectedValueName;
    }
}