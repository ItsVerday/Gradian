package gg.valgo.gradian.examples.expressioneval.ast;

import gg.valgo.gradian.examples.expressioneval.EvaluationContext;
import gg.valgo.gradian.examples.expressioneval.ExpressionAST;

public class ASTVariable extends ExpressionAST {
    private String identifier;

    public ASTVariable(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public double evaluate(EvaluationContext ctx) {
        return ctx.getVariable(identifier);
    }
}