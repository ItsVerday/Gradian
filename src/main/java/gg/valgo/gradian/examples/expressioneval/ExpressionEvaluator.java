package gg.valgo.gradian.examples.expressioneval;

import gg.valgo.gradian.Gradian;
import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserException;
import gg.valgo.gradian.examples.expressioneval.ast.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ExpressionEvaluator {
    public static final Parser<String> openParenthesis = Gradian.string("(");
    public static final Parser<String> closeParenthesis = Gradian.string(")");
    public static final Parser<String> dot = Gradian.string(".");
    public static final Parser<String> minus = Gradian.string("-");
    public static final Parser<String> plusOrMinus = Gradian.choice(Gradian.string("+"), minus);
    public static final Parser<String> comma = Gradian.string(",");
    public static final Parser<String> equalSign = Gradian.string("=");
    public static final Parser<String> semicolon = Gradian.string(";");

    public static final Parser<ExpressionAST> value = Gradian.recursive(() -> Gradian.anyTypeChoice(
            ExpressionEvaluator.number,
            Gradian.between(openParenthesis, closeParenthesis, ExpressionEvaluator.additives),
            Gradian.anyTypeSequence(minus.ignore(), ExpressionEvaluator.value).index(0).<ExpressionAST>castMap().map(ASTNegation::new),
            ExpressionEvaluator.call,
            ExpressionEvaluator.variable
    ).castMap());

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

    public static final Parser<ASTNumber> number = Gradian.choice(
            scientificNotation,
            floatParser,
            integerParser
    ).map(ASTNumber::new);

    public static final Parser<String> identifier = Gradian.regex("[A-Za-z_][A-Za-z0-9_]*");
    public static final Parser<ASTVariable> variable = identifier.map(ASTVariable::new);

    public static final Parser<ExpressionAST> powers = Gradian.coroutine(ctx -> {
        ArrayList<ExpressionAST> values = new ArrayList<>(Arrays.asList(ctx.yield(value)));
        ctx.yield(Gradian.optionalWhitespace);

        while (ctx.yield(Gradian.peekCharacter).equals("^")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);

            values.add(ctx.yield(value));

            ctx.yield(Gradian.optionalWhitespace);
        }

        ExpressionAST ast = values.get(values.size() - 1);
        for (int i = values.size() - 2; i >= 0; i--) {
            ast = new ASTPower(values.get(i), ast);
        }

        return ast;
    });

    public static final Parser<ExpressionAST> multiplicatives = Gradian.coroutine(ctx -> {
        ExpressionAST ast = ctx.yield(powers);
        ctx.yield(Gradian.optionalWhitespace);

        String operator;
        while ((operator = ctx.yield(Gradian.peekCharacter)).equals("*") || operator.equals("/") || operator.equals("%")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);

            ExpressionAST otherAST = ctx.yield(powers);
            switch (operator) {
                case "*": ast = new ASTMultiplication(ast, otherAST); break;
                case "/": ast = new ASTDivision(ast, otherAST); break;
                case "%": ast = new ASTModulo(ast, otherAST); break;
            }

            ctx.yield(Gradian.optionalWhitespace);
        }

        return ast;
    });

    public static final Parser<ExpressionAST> additives = Gradian.coroutine(ctx -> {
        ExpressionAST ast = ctx.yield(multiplicatives);
        ctx.yield(Gradian.optionalWhitespace);

        String operator;
        while ((operator = ctx.yield(Gradian.peekCharacter)).equals("+") || operator.equals("-")) {
            ctx.yield(Gradian.anyCharacter);
            ctx.yield(Gradian.optionalWhitespace);

            ExpressionAST otherAST = ctx.yield(multiplicatives);
            switch (operator) {
                case "+": ast = new ASTAddition(ast, otherAST); break;
                case "-": ast = new ASTSubtraction(ast, otherAST); break;
            }

            ctx.yield(Gradian.optionalWhitespace);
        }

        return ast;
    });

    public static final Parser<ASTCall> call = Gradian.coroutine(ctx -> {
        String name = ctx.yield(identifier);
        ctx.yield(Gradian.optionalWhitespace);
        ctx.yield(openParenthesis);
        ctx.yield(Gradian.optionalWhitespace);
        ArrayList<ExpressionAST> args = ctx.yield(Gradian.separatedBy(Gradian.between(Gradian.optionalWhitespace, Gradian.optionalWhitespace, Gradian.string(",")), value).asArrayList());
        ctx.yield(Gradian.optionalWhitespace);
        ctx.yield(closeParenthesis);
        ctx.yield(Gradian.optionalWhitespace);

        return new ASTCall(name, args);
    });

    public static final Parser<FunctionDefinition> functionDefinition = Gradian.coroutine(ctx -> {
        ctx.yield(Gradian.optionalWhitespace);
        String name = ctx.yield(identifier);
        ctx.yield(Gradian.optionalWhitespace);
        ctx.yield(openParenthesis);
        ctx.yield(Gradian.optionalWhitespace);
        ArrayList<String> arguments = ctx.yield(Gradian.separatedBy(Gradian.between(Gradian.optionalWhitespace, Gradian.optionalWhitespace, Gradian.string(",")), identifier).asArrayList());
        ctx.yield(Gradian.optionalWhitespace);
        ctx.yield(closeParenthesis);
        ctx.yield(Gradian.optionalWhitespace);
        ctx.yield(equalSign);
        ctx.yield(Gradian.optionalWhitespace);
        ExpressionAST ast = ctx.yield(additives);
        ctx.yield(semicolon);

        return new FunctionDefinition((context, args) -> {
            EvaluationContext newContext = EvaluationContext.copy(context);

            int index = 0;
            for (String arg : arguments) {
                newContext.setVariable(arg, args.get(index++));
            }

            return ast.evaluate(newContext);
        }, name);
    });

    public static final Parser<Expression> expression = Gradian.coroutine(ctx -> {
        ArrayList<FunctionDefinition> functions = ctx.yield(Gradian.many(functionDefinition).asArrayList());
        ctx.yield(Gradian.optionalWhitespace);
        ctx.yield(Gradian.string("return"));
        ctx.yield(Gradian.optionalWhitespace);
        ExpressionAST ast = ctx.yield(additives);

        HashMap<String, Function> definedFunctions = new HashMap<>();
        for (FunctionDefinition f : functions) {
            definedFunctions.put(f.getName(), f.getFunction());
        }

        return new Expression(definedFunctions, ast);
    });

    public static Expression evaluate(String input) throws ParserException {
        return expression.getResult(input);
    }

    public static void main(String[] args) throws ParserException {
        String expression = "something(a, b, c) = a * b + c; return something(1, 2, 3)";
        Expression exp = evaluate(expression);
        System.out.println(exp.evaluate(new EvaluationContext().setVariable("y", 12)));
    }
}