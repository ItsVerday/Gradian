package gg.valgo.gradian.examples.expressioneval.ast;

import gg.valgo.gradian.examples.expressioneval.EvaluationContext;
import gg.valgo.gradian.examples.expressioneval.ExpressionAST;

public class ASTAddition extends ExpressionAST {
    private ExpressionAST a;
    private ExpressionAST b;

    public ASTAddition(ExpressionAST a, ExpressionAST b) {
        this.a = a;
        this.b = b;
    }

    public ExpressionAST getA() {
        return a;
    }

    public ExpressionAST getB() {
        return b;
    }

    @Override
    public double evaluate(EvaluationContext ctx) {
        return a.evaluate(ctx) + b.evaluate(ctx);
    }
}