package com.interpreter.parser.ast.expressions;

import com.interpreter.parser.ast.expressions.Expression;
import com.interpreter.parser.variables.Value;
import com.interpreter.parser.variables.Variables;

public class VariableExpression implements Expression {

    //    private Value value;
    private String varName;

    public VariableExpression(String varName) {
        this.varName = varName;
    }

    @Override
    public Value calculate() {
        if (!Variables.isExists(varName))
            throw new RuntimeException("Variable does not exist! " + varName);
        else
            return Variables.getValue(varName);
    }

    @Override
    public String toString() {
        return varName;
    }
}