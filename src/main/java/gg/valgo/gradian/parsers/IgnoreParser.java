package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class IgnoreParser<ResultType> extends Parser<ResultType> {
    private Parser<ResultType> parser;

    public IgnoreParser(Parser<ResultType> parser) {
        this.parser = parser;
    }

    public Parser<?> getParser() {
        return parser;
    }

    @Override
    public ParserState<ResultType> parse(ParserState<?> state) {
        return parser.parse(state).<ResultType>updateType().setIgnoreResult(true);
    }

    @Override
    public String getParserName() {
        return parser.getParserName();
    }

    @Override
    public String getExpectedValueName() {
        return parser.getExpectedValueName();
    }
}