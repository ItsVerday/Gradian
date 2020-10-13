package gg.valgo.gradian.examples.expressioneval;

public class FunctionDefinition {
    private Function function;
    private String name;

    public FunctionDefinition(Function function, String name) {
        this.function = function;
        this.name = name;
    }

    public Function getFunction() {
        return function;
    }

    public String getName() {
        return name;
    }
}