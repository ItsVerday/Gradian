package gg.valgo.gradian.input;

import gg.valgo.gradian.ParserInputList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenInputList extends ParserInputList<Token> {
    private ArrayList<Token> tokens;

    public TokenInputList(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public TokenInputList(Token[] tokens) {
        this(new ArrayList<>(Arrays.asList(tokens)));
    }

    @Override
    public List<Token> getElements() {
        return tokens;
    }
}