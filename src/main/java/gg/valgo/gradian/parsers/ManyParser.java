package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

import java.util.ArrayList;
import java.util.Arrays;

public class ManyParser<ResultType> extends Parser<ResultType[]> {
    private Parser<ResultType> parser;
    private int minimumCount;
    private int maximumCount;

    public ManyParser(Parser<ResultType> parser, int minimumCount, int maximumCount) {
        this.parser = parser;
        this.minimumCount = minimumCount;
        this.maximumCount = maximumCount;

        if (minimumCount > maximumCount && minimumCount != -1 && maximumCount != -1) {
            int temp = minimumCount;
            this.minimumCount = maximumCount;
            this.maximumCount = temp;
        }
    }

    public Parser<?> getParser() {
        return parser;
    }

    public int getMinimumCount() {
        return minimumCount;
    }

    public int getMaximumCount() {
        return maximumCount;
    }

    @Override
    public ParserState<ResultType[]> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        ParserState<ResultType> currentState = state.updateType();
        ArrayList<ResultType> results = new ArrayList<>();
        int parseCount = 0;

        while (!currentState.isException()) {
            currentState = parser.parse(currentState);
            if (!currentState.isException() && !currentState.isIgnoreResult()) {
                results.add(currentState.getResult());
            }

            if (!currentState.isException()) {
                parseCount++;

                if (parseCount > maximumCount && maximumCount != -1) {
                    break;
                }
            }
        }

        if ((parseCount < minimumCount && minimumCount != -1) || (parseCount > maximumCount && maximumCount != -1)) {
            return state.<ResultType[]>updateType().formatException(this, parseCount + " of " + parser.getExpectedValueName());
        }

        return currentState.setException(null).updateType().updateState(currentState.getIndex(), (ResultType[]) results.toArray());
    }

    public Parser<ArrayList<ResultType>> asArrayList() {
        return map(array -> new ArrayList<>(Arrays.asList(array)));
    }

    public Parser<String> join(String delimiter) {
        return map(array -> {
            String joined = "";
            for (ResultType elt : array) {
                joined = joined.concat(elt.toString() + delimiter);
            }

            return joined.substring(0, joined.length() - delimiter.length());
        });
    }

    @Override
    public String getExpectedValueName() {
        if (minimumCount == -1 && maximumCount == -1) {
            return "any amount of " + parser.getExpectedValueName();
        }

        if (minimumCount == -1) {
            return "at most " + maximumCount + " of " + parser.getExpectedValueName();
        }

        if (maximumCount == -1) {
            return "at least " + minimumCount + " of " + parser.getExpectedValueName();
        }

        if (minimumCount == maximumCount) {
            return "exactly " + minimumCount + " of " + parser.getExpectedValueName();
        }

        return "between " + minimumCount + " and " + maximumCount + " of " + parser.getExpectedValueName();
    }
}