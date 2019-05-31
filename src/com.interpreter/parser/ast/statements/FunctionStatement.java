package com.interpreter.parser.ast.statements;

import com.interpreter.parser.ast.expressions.FunctionalExpression;

public class FunctionStatement implements Statement {


    private FunctionalExpression function;

    public FunctionStatement(FunctionalExpression function) {
        this.function = function;
    }

    @Override
    public void execute() {
        function.calculate();
    }
}
