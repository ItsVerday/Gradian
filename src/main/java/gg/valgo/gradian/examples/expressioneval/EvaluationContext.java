package gg.valgo.gradian.examples.expressioneval;

import java.util.HashMap;
import java.util.Random;
import java.util.function.BinaryOperator;

public class EvaluationContext {
    private Random random = new Random();

    private HashMap<String, Double> variables = new HashMap<>();
    private HashMap<String, Function> functions = new HashMap<>();

    public EvaluationContext() {
        setFunction("abs", (ctx, args) -> Math.abs(args.get(0)));
        setFunction("abs", (ctx, args) -> Math.abs(args.get(0)));
        setFunction("floor", (ctx, args) -> Math.floor(args.get(0)));
        setFunction("ceil", (ctx, args) -> Math.ceil(args.get(0)));
        setFunction("round", (ctx, args) -> (double) Math.round(args.get(0)));
        setFunction("sqrt", (ctx, args) -> Math.sqrt(args.get(0)));
        setFunction("cbrt", (ctx, args) -> Math.cbrt(args.get(0)));
        setFunction("min", (ctx, args) -> args.stream().reduce(Double.POSITIVE_INFINITY, BinaryOperator.minBy(Double::compareTo)));
        setFunction("max", (ctx, args) -> args.stream().reduce(Double.NEGATIVE_INFINITY, BinaryOperator.maxBy(Double::compareTo)));
        setFunction("deg", (ctx, args) -> Math.toDegrees(args.get(0)));
        setFunction("rad", (ctx, args) -> Math.toRadians(args.get(0)));
        setFunction("sin", (ctx, args) -> Math.sin(args.get(0)));
        setFunction("cos", (ctx, args) -> Math.cos(args.get(0)));
        setFunction("tan", (ctx, args) -> Math.tan(args.get(0)));
        setFunction("csc", (ctx, args) -> 1 / Math.sin(args.get(0)));
        setFunction("sec", (ctx, args) -> 1 / Math.cos(args.get(0)));
        setFunction("cot", (ctx, args) -> 1 / Math.tan(args.get(0)));
        setFunction("asin", (ctx, args) -> Math.asin(args.get(0)));
        setFunction("acos", (ctx, args) -> Math.acos(args.get(0)));
        setFunction("atan", (ctx, args) -> Math.atan(args.get(0)));
        setFunction("atan2", (ctx, args) -> Math.atan2(args.get(0), args.get(1)));
        setFunction("acsc", (ctx, args) -> Math.asin(1 / args.get(0)));
        setFunction("asec", (ctx, args) -> Math.acos(1 / args.get(0)));
        setFunction("acot", (ctx, args) -> Math.atan(1 / args.get(0)));
        setFunction("fib", (ctx, args) -> {
            double count = args.get(0);
            long a = 0;
            long b = 1;
            for (int i = 0; i < count; i++) {
                long temp = a;
                a = b;
                b = b + temp;
            }

            return (double) a;
        });
        setFunction("rand", (ctx, args) -> {
            double min, max;
            switch (args.size()) {
                case 0: min = 0; max = 1; break;
                case 1: min = 0; max = args.get(0); break;
                default: min = args.get(0); max = args.get(1); break;
            }

            return random.nextDouble() * (max - min) + min;
        });
        setFunction("gaussian", (ctx, args) -> {
            double stddev, offset;
            switch (args.size()) {
                case 0: stddev = 1; offset = 0; break;
                case 1: stddev = args.get(0); offset = 0; break;
                default: stddev = args.get(0); offset = args.get(1); break;
            }

            return random.nextGaussian() * stddev + offset;
        });
    }

    public static EvaluationContext copy(EvaluationContext ctx) {
        EvaluationContext newCtx = new EvaluationContext();
        for (String variable : ctx.variables.keySet()) {
            newCtx.setVariable(variable, ctx.getVariable(variable));
        }

        for (String function : ctx.functions.keySet()) {
            newCtx.setFunction(function, ctx.getFunction(function));
        }

        return newCtx;
    }

    public EvaluationContext setVariable(String name, double value) {
        variables.put(name, value);
        return this;
    }

    public double getVariable(String name) {
        return variables.get(name);
    }

    public EvaluationContext setFunction(String name, Function function) {
        functions.put(name, function);
        return this;
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }
}