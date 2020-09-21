package gg.valgo.gradian.examples;

import gg.valgo.gradian.Gradian;
import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserException;

import java.util.ArrayList;

public class ParserTesting {
    public static final Parser<Integer> number = Gradian.digits.map(Integer::parseInt);
    public static final Parser<String> letters = Gradian.coroutine(ctx -> {
        int count = (int) ctx.yield(number);
        ctx.yield(Gradian.string("|"));
        String letters = "";

        for (int i = 0; i < count; i++) {
            letters = letters.concat((String) ctx.yield(Gradian.letter));
        }

        return letters;
    });

    public static final Parser<ArrayList<String>> manyLetters = Gradian.many(letters).asArrayList();
    public static final Parser<ArrayList<String>> lettersParser = Gradian.sequence(manyLetters, Gradian.endOfInput).map(result -> result[0]).mapType();

    public static void main(String[] args) throws ParserException {
        System.out.println(lettersParser.getResult("1|a1|b1|c"));
    }
}