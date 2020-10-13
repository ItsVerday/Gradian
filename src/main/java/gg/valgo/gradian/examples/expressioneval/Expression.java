package gg.valgo.gradian.examples.expressioneval;

import java.util.HashMap;

public class Expression {
    private HashMap<String, Function> definedFunctions;
    private ExpressionAST expression;

    public Expression(HashMap<String, Function> definedFunctions, ExpressionAST expression) {
        this.definedFunctions = definedFunctions;
        this.expression = expression;
    }

    public double evaluate(EvaluationContext ctx) {
        for (String key : definedFunctions.keySet()) {
            ctx.setFunction(key, definedFunctions.get(key));
        }

        return expression.evaluate(ctx);
    }
}