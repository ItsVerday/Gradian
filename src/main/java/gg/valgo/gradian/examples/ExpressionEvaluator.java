package gg.valgo.gradian.examples;

import gg.valgo.gradian.Gradian;
import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserException;

import java.util.ArrayList;
import java.util.Random;

public class ExpressionEvaluator {
    private static final Random random = new Random();

    public static final Parser<String> dot = Gradian.string(".");
    public static final Parser<String> openParenthesis = Gradian.string("(");
    public static final Parser<String> closeParenthesis = Gradian.string(")");
    public static final Parser<String> scientificNotationSeparator = Gradian.choice(Gradian.string("E"), Gradian.string("e")).mapType();

    public static final Parser<Double> value = Gradian.recursive(() -> Gradian.choice(
            ExpressionEvaluator.negate,
            ExpressionEvaluator.functions,
            Gradian.between(openParenthesis, closeParenthesis, ExpressionEvaluator.additives),
            ExpressionEvaluator.number
    ).mapType());

    public static final Parser<String> plusOrMinus = Gradian.choice(Gradian.string("+"), Gradian.string("-")).mapType();
    public static final Parser<String> optionalPlusOrMinus = Gradian.maybe(plusOrMinus).valueIfAbsent("");
    public static final Parser<Double> integerParser = Gradian.sequence(
            optionalPlusOrMinus,
            Gradian.digits,
            Gradian.maybe(dot)
    ).map(values -> (values[0].equals("-") ? "-" : "") + values[1]).map(Double::parseDouble);
    public static final Parser<Double> floatWithWhole = Gradian.sequence(
            optionalPlusOrMinus,
            Gradian.digits,
            dot,
            Gradian.digits
    ).map(values -> (values[0].equals("-") ? "-" : "") + values[1] + values[2] + values[3]).map(Double::parseDouble);
    public static final Parser<Double> floatWithoutWhole = Gradian.sequence(
            optionalPlusOrMinus,
            dot,
            Gradian.digits
    ).map(values -> (values[0].equals("-") ? "-" : "") + "0" + values[1] + values[2]).map(Double::parseDouble);
    public static final Parser<Double> floatParser = Gradian.choice(floatWithoutWhole, floatWithWhole).mapType();
    public static final Parser<Double> integerOrFloat = Gradian.choice(floatParser, integerParser).mapType();
    public static final Parser<Double> scientificNotationParser = Gradian.sequence(
            integerOrFloat,
            scientificNotationSeparator,
            integerOrFloat
    ).map(values -> (double) values[0] * Math.pow(10, (double) values[2]));
    public static final Parser<Double> number = Gradian.choice(scientificNotationParser, integerOrFloat).mapType();

    public static final Parser<Double> negate = Gradian.sequence(
            Gradian.ignore(Gradian.string("-")),
            value
    ).map(value -> (double) value[0] * -1);

    public static final Parser<Double> exponents = Gradian.coroutine(ctx -> {
        ArrayList<Double> values = new ArrayList<>();
        values.add((double) ctx.yield(value));
        ctx.yield(Gradian.optionalWhitespace);

        while (ctx.yield(Gradian.peek1).equals("^")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);
            values.add((double) ctx.yield(value));
            ctx.yield(Gradian.optionalWhitespace);
        }

        double result = values.get(values.size() - 1);
        for (int index = values.size() - 2; index >= 0; index--) {
            result = Math.pow(values.get(index), result);
        }

        return result;
    });

    public static final Parser<Double> multiplicatives = Gradian.coroutine(ctx -> {
        double result = (double) ctx.yield(exponents);
        ctx.yield(Gradian.optionalWhitespace);

        String operator;
        while ((operator = (String) ctx.yield(Gradian.peek1)).equals("*") || operator.equals("/") || operator.equals("%")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);
            double otherOperand = (double) ctx.yield(exponents);
            ctx.yield(Gradian.optionalWhitespace);

            switch (operator) {
                case "*": result *= otherOperand; break;
                case "/": result /= otherOperand; break;
                case "%": result %= otherOperand; break;
            }
        }

        return result;
    });

    public static final Parser<Double> additives = Gradian.coroutine(ctx -> {
        double result = (double) ctx.yield(multiplicatives);
        ctx.yield(Gradian.optionalWhitespace);

        String operator;
        while ((operator = (String) ctx.yield(Gradian.peek1)).equals("+") || operator.equals("-")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);
            double otherOperand = (double) ctx.yield(multiplicatives);
            ctx.yield(Gradian.optionalWhitespace);

            switch (operator) {
                case "+": result += otherOperand; break;
                case "-": result -= otherOperand; break;
            }
        }

        return result;
    });

    public static final Parser<Double> functions = Gradian.choice(
            function("abs", Math::abs),
            function("floor", Math::floor),
            function("ceil", Math::ceil),
            function("round", value -> (double) Math.round(value)),
            function("sqrt", Math::sqrt),
            function("cbrt", Math::cbrt),
            function("deg", Math::toDegrees),
            function("rad", Math::toRadians),
            function("sin", Math::sin),
            function("cos", Math::cos),
            function("tan", Math::tan),
            function("rand", value -> random.nextDouble() * value)
    ).mapType();

    public static Parser<Double> function(String name, Parser.ResultMapper<Double, Double> mapper) {
        return Gradian.sequence(
                Gradian.ignore(Gradian.string(name)),
                Gradian.between(openParenthesis, closeParenthesis, additives)
        ).map(values -> (double) values[0]).map(mapper);
    }

    public static void main(String[] args) throws ParserException {
        String exp = "1 + 2 * 3 / 4 - 5 % 6";
        System.out.println(evaluate(exp));
    }

    public static double evaluate(String exp) throws ParserException {
        return additives.getResult(exp);
    }
}