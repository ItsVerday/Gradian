package gg.valgo.gradian.examples.expressioneval;

import java.util.ArrayList;

public interface Function {
    double evaluate(EvaluationContext ctx, ArrayList<Double> values);
}