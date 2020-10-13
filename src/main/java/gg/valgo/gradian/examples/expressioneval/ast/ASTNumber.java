package gg.valgo.gradian.examples.expressioneval.ast;

import gg.valgo.gradian.examples.expressioneval.EvaluationContext;
import gg.valgo.gradian.examples.expressioneval.ExpressionAST;

public class ASTNumber extends ExpressionAST {
    private double number;

    public ASTNumber(double number) {
        this.number = number;
    }

    public double getNumber() {
        return number;
    }

    @Override
    public double evaluate(EvaluationContext ctx) {
        return number;
    }
}