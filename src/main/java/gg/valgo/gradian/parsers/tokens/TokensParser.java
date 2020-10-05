package gg.valgo.gradian.parsers.tokens;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.Token;
import gg.valgo.gradian.input.TokenInputList;

import java.util.ArrayList;
import java.util.Arrays;

public class TokensParser extends Parser<ArrayList<Token>> {
    private Token[] tokens;

    public TokensParser(Token... tokens) {
        this.tokens = tokens;
    }

    public Token[] getTokens() {
        return tokens;
    }

    @Override
    public ParserState<ArrayList<Token>> parse(ParserState<?> state) {
        if (!(state.getInput() instanceof TokenInputList)) {
            return state.badInputType(this).updateType();
        }

        if (state.isException()) {
            return state.updateType();
        }

        TokenInputList input = (TokenInputList) state.getInput();

        if (input.getElements().size() < tokens.length) {
            return state.formatException(this, "end of input").updateType();
        }

        int index = 0;
        for (Token token : tokens) {
            if (!token.equals(input.getTruncated().get(index++))) {
                return state.formatException(this, "token " + token).updateType();
            }
        }

        return state.updateState(state.getIndex() + tokens.length, new ArrayList<>(Arrays.asList(tokens)));
    }

    @Override
    public String getParserName() {
        return super.getParserName();
    }

    @Override
    public String getExpectedValueName() {
        return "tokens " + new ArrayList<>(Arrays.asList(tokens));
    }
}