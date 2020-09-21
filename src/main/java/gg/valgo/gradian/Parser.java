package gg.valgo.gradian;

public abstract class Parser<ResultType> {
    private String parserName;
    public abstract ParserState<ResultType> parse(ParserState<?> state);
    public abstract String getExpectedValueName();

    public String getParserName() {
        return parserName;
    }

    public Parser<ResultType> setParserName(String parserName) {
        this.parserName = parserName;
        return this;
    }

    public ParserState<ResultType> run(String input) {
        return parse(new ParserState<ResultType>(input));
    }

    public ResultType getResult(String input) throws ParserException {
        ParserState<ResultType> state = run(input);
        if (state.isException()) {
            throw state.getException();
        }

        return state.getResult();
    }

    public <NewResultType> Parser<NewResultType> mapState(ParserState.StateMapper<ResultType, NewResultType> mapper) {
        final Parser<ResultType> self = this;

        return new Parser<>() {
            @Override
            public ParserState<NewResultType> parse(ParserState<?> state) {
                ParserState<ResultType> newState = self.parse(state);

                if (newState.isException()) {
                    return newState.updateType();
                }

                return mapper.map(newState);
            }

            @Override
            public String getParserName() {
                return self.getParserName();
            }

            @Override
            public String getExpectedValueName() {
                return self.getExpectedValueName();
            }
        };
    }

    public <NewResultType> Parser<NewResultType> map(final ResultMapper<ResultType, NewResultType> mapper) {
        final Parser<ResultType> self = this;

        return new Parser<>() {
            @Override
            public ParserState<NewResultType> parse(ParserState<?> state) {
                ParserState<ResultType> newState = self.parse(state);

                if (newState.isException()) {
                    return newState.updateType();
                }

                return newState.updateState(newState.getIndex(), mapper.map(newState.getResult())).setIgnoreResult(newState.isIgnoreResult());
            }

            @Override
            public String getParserName() {
                return self.getParserName();
            }

            @Override
            public String getExpectedValueName() {
                return self.getExpectedValueName();
            }
        };
    }

    public <NewResultType> Parser<NewResultType> mapType() {
        return map(value -> (NewResultType) value);
    }

    public interface ResultMapper<OldResultType, NewResultType> {
        NewResultType map(OldResultType object);
    }
}