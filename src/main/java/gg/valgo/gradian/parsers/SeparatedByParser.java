package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

import java.util.ArrayList;
import java.util.Arrays;

public class SeparatedByParser<ResultType> extends Parser<ResultType[]> {
    private Parser<?> separator;
    private Parser<ResultType> values;

    public SeparatedByParser(Parser<?> separator, Parser<ResultType> values) {
        this.separator = separator;
        this.values = values;
    }

    public Parser<?> getSeparator() {
        return separator;
    }

    public Parser<?> getValues() {
        return values;
    }

    @Override
    public ParserState<ResultType[]> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        ArrayList<Object> results = new ArrayList<>();
        ParserState<?> nextState = state;
        ParserState<?> error = null;

        while (true) {
            ParserState<?> valueState = values.parse(nextState);
            ParserState<?> separatorState = separator.parse(valueState);

            if (valueState.isException()) {
                error = valueState;
                break;
            } else {
                if (!valueState.isIgnoreResult()) {
                    results.add(valueState.getResult());
                }
            }

            if (separatorState.isException()) {
                nextState = valueState;
                break;
            }

            nextState = separatorState;
        }

        if (error != null) {
            if (results.size() == 0) {
                return state.<ResultType[]>updateType().updateState(state.getIndex(), (ResultType[]) results.toArray());
            }

            return error.updateType();
        }

            return nextState.<ResultType[]>updateType().updateState(nextState.getIndex(), (ResultType[]) results.toArray());
    }

    public Parser<ArrayList<ResultType>> asArrayList() {
        return map(array -> new ArrayList<>(Arrays.asList(array)));
    }

    public Parser<String> join(String delimiter) {
        return map(array -> {
            String joined = "";
            for (Object elt : array) {
                joined = joined.concat(elt.toString() + delimiter);
            }

            return joined.substring(0, joined.length() - delimiter.length());
        });
    }

    @Override
    public String getParserName() {
        return "separatedBy";
    }

    @Override
    public String getExpectedValueName() {
        return "instances of " + values.getExpectedValueName() + " separated by " + separator.getExpectedValueName();
    }
}