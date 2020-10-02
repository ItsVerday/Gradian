package gg.valgo.gradian.examples;

import gg.valgo.gradian.Gradian;
import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserException;

public class ParserTesting {
    public static void main(String[] args) throws ParserException {
        Parser<Long> longParser = Gradian.digits.map(result -> Integer.parseInt(result)).mapType();
        // longParser gets an int and maps it to a long.
        System.out.println(longParser.getResult("123456789")); // 123456789
    }
}