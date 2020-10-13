package gg.valgo.gradian.examples.expressioneval.ast;

import gg.valgo.gradian.examples.expressioneval.EvaluationContext;
import gg.valgo.gradian.examples.expressioneval.ExpressionAST;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ASTCall extends ExpressionAST {
    private String identifier;
    private ArrayList<ExpressionAST> args;

    public ASTCall(String identifier, ArrayList<ExpressionAST> args) {
        this.identifier = identifier;
        this.args = args;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<ExpressionAST> getArgs() {
        return args;
    }

    @Override
    public double evaluate(EvaluationContext ctx) {
        ArrayList<Double> arguments = args.stream().map(value -> value.evaluate(ctx)).collect(Collectors.toCollection(ArrayList::new));
        return ctx.getFunction(identifier).evaluate(ctx, arguments);
    }
}