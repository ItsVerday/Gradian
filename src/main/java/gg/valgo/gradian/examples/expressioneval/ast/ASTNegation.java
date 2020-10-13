package gg.valgo.gradian.examples.expressioneval.ast;

import gg.valgo.gradian.examples.expressioneval.EvaluationContext;
import gg.valgo.gradian.examples.expressioneval.ExpressionAST;

public class ASTNegation extends ExpressionAST {
    private ExpressionAST value;

    public ASTNegation(ExpressionAST value) {
        this.value = value;
    }

    public ExpressionAST getValue() {
        return value;
    }

    @Override
    public double evaluate(EvaluationContext ctx) {
        return -value.evaluate(ctx);
    }
}