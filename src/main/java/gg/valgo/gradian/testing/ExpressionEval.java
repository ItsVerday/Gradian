package gg.valgo.gradian.testing;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserException;
import gg.valgo.gradian.Gradian;
import gg.valgo.gradian.util.interfaces.ParserResultMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.BinaryOperator;

public class ExpressionEval {
    private static final Random random = new Random();

    public static final Parser<String> openParenthesis = Gradian.string("(");
    public static final Parser<String> closeParenthesis = Gradian.string(")");
    public static final Parser<String> dot = Gradian.string(".");
    public static final Parser<String> plusOrMinus = Gradian.choice(Gradian.string("+"), Gradian.string("-"));
    public static final Parser<String> comma = Gradian.string(",");

    public static final Parser<Double> value = Gradian.recursive(() -> Gradian.choice(
            ExpressionEval.functions,
            Gradian.anyTypeSequence(Gradian.string("-").ignore(), ExpressionEval.value).index(0).<Double>castMap().map(value -> -value),
            ExpressionEval.number,
            Gradian.anyTypeSequence(openParenthesis.ignore(), ExpressionEval.additives, closeParenthesis.ignore()).index(0).castMap()
    ));

    public static final Parser<Double> integerParser = Gradian.sequence(
            Gradian.maybe(plusOrMinus).valueIfAbsent(""),
            Gradian.digits,
            Gradian.maybe(dot).ignore()
    ).join("").map(Double::parseDouble);

    public static final Parser<Double> floatParser = Gradian.sequence(
            Gradian.maybe(plusOrMinus).valueIfAbsent(""),
            Gradian.maybe(Gradian.digits).valueIfAbsent("0"),
            dot,
            Gradian.digits
    ).join("").map(Double::parseDouble);

    public static final Parser<Double> scientificNotation = Gradian.anyTypeSequence(
            Gradian.choice(
                    floatParser,
                    integerParser
            ),
            Gradian.choice(
                    Gradian.string("e"),
                    Gradian.string("E")
            ).ignore(),
            integerParser
    ).map(values -> (double) values[0] * Math.pow(10, (double) values[1]));

    public static final Parser<Double> number = Gradian.choice(
            scientificNotation,
            floatParser,
            integerParser
    );

    public static final Parser<Double> powers = Gradian.coroutine(ctx -> {
        ArrayList<Double> values = new ArrayList<>(Collections.singletonList(ctx.yield(ExpressionEval.value)));
        ctx.yield(Gradian.optionalWhitespace);

        while (ctx.yield(Gradian.peekCharacter).equals("^")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);
            values.add(ctx.yield(ExpressionEval.value));
            ctx.yield(Gradian.optionalWhitespace);
        }

        double value = values.get(values.size() - 1);
        for (int index = values.size() - 2; index >= 0; index--) {
            value = Math.pow(values.get(index), value);
        }

        return value;
    });

    public static final Parser<Double> multiplicatives = Gradian.coroutine(ctx -> {
        double value = ctx.yield(powers);
        ctx.yield(Gradian.optionalWhitespace);

        String operator;
        while ((operator = ctx.yield(Gradian.peekCharacter)).equals("*") || operator.equals("/") || operator.equals("%")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);

            double otherValue = ctx.yield(powers);
            switch (operator) {
                case "*": value *= otherValue; break;
                case "/": value /= otherValue; break;
                case "%": value %= otherValue; break;
            }

            ctx.yield(Gradian.optionalWhitespace);
        }

        return value;
    });

    public static final Parser<Double> additives = Gradian.coroutine(ctx -> {
        double value = ctx.yield(multiplicatives);
        ctx.yield(Gradian.optionalWhitespace);

        String operator;
        while ((operator = ctx.yield(Gradian.peekCharacter)).equals("+") || operator.equals("-")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);

            double otherValue = ctx.yield(multiplicatives);
            switch (operator) {
                case "+": value += otherValue; break;
                case "-": value -= otherValue; break;
            }

            ctx.yield(Gradian.optionalWhitespace);
        }

        return value;
    });

    public static final Parser<Double> functions = Gradian.choice(
            function("abs", args -> Math.abs(args.get(0))),
            function("floor", args -> Math.floor(args.get(0))),
            function("ceil", args -> Math.ceil(args.get(0))),
            function("round", args -> (double) Math.round(args.get(0))),
            function("sqrt", args -> Math.sqrt(args.get(0))),
            function("cbrt", args -> Math.cbrt(args.get(0))),
            function("min", args -> args.stream().reduce(Double.POSITIVE_INFINITY, BinaryOperator.minBy(Double::compareTo))),
            function("max", args -> args.stream().reduce(Double.NEGATIVE_INFINITY, BinaryOperator.maxBy(Double::compareTo))),
            function("deg", args -> Math.toDegrees(args.get(0))),
            function("rad", args -> Math.toRadians(args.get(0))),
            function("sin", args -> Math.sin(args.get(0))),
            function("cos", args -> Math.cos(args.get(0))),
            function("tan", args -> Math.tan(args.get(0))),
            function("csc", args -> 1 / Math.sin(args.get(0))),
            function("sec", args -> 1 / Math.cos(args.get(0))),
            function("cot", args -> 1 / Math.sin(args.get(0))),
            function("asin", args -> Math.asin(args.get(0))),
            function("acos", args -> Math.acos(args.get(0))),
            function("atan", args -> Math.atan(args.get(0))),
            function("atan2", args -> Math.atan2(args.get(0), args.get(1))),
            function("acsc", args -> Math.asin(1 / args.get(0))),
            function("asec", args -> Math.acos(1 / args.get(0))),
            function("acot", args -> Math.asin(1 / args.get(0))),
            function("rand", args -> {
                double min, max;
                switch (args.size()) {
                    case 0: min = 0; max = 1; break;
                    case 1: min = 0; max = args.get(0); break;
                    default: min = args.get(0); max = args.get(1); break;
                }

                return random.nextDouble() * (max - min) + min;
            }),
            function("gaussian", args -> {
                double stddev, offset;
                switch (args.size()) {
                    case 0: stddev = 1; offset = 0; break;
                    case 1: stddev = args.get(0); offset = 0; break;
                    default: stddev = args.get(0); offset = args.get(1); break;
                }

                return random.nextGaussian() * stddev + offset;
            })
    );

    public static double eval(String exp) throws ParserException {
        return additives.getResult(exp);
    }

    public static void main(String[] args) throws ParserException {
        System.out.println(eval("1"));
    }

    public static Parser<Double> function(String name, ParserResultMapper<ArrayList<Double>, Double> func) {
        return Gradian.anyTypeSequence(
                Gradian.string(name).ignore(),
                Gradian.optionalWhitespace.ignore(),
                openParenthesis.ignore(),
                Gradian.optionalWhitespace.ignore(),
                Gradian.maybe(
                        Gradian.anyTypeSequence(
                                value,
                                Gradian.many(
                                        Gradian.anyTypeSequence(
                                                Gradian.optionalWhitespace,
                                                comma,
                                                Gradian.optionalWhitespace,
                                                value
                                        ).index(3).<Double>castMap()
                                ).asArrayList()
                        ).map(values -> {
                            ArrayList<Double> args = new ArrayList<>();
                            args.add((double) values[0]);
                            args.addAll((ArrayList<Double>) values[1]);
                            return args;
                        })
                ).valueIfAbsent(new ArrayList<>()).map(func),
                Gradian.optionalWhitespace.ignore(),
                closeParenthesis.ignore()
        ).index(0).castMap();
    }
}