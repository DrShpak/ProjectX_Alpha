package com.interpreter.parser.ast.expressions;

import com.interpreter.parser.variables.ArrayValue;
import com.interpreter.parser.variables.Value;
import com.interpreter.parser.variables.Variables;

public class ArrayAccessExpression implements Expression {

    private final String variable;
    private final Expression index;

    public ArrayAccessExpression(String variable, Expression index) {
        this.variable = variable;
        this.index = index;
    }

    @Override
    public Value calculate() {
        final Value var = Variables.getValue(variable);
        if (var instanceof ArrayValue) {
            final ArrayValue array = (ArrayValue) var;
            return array.get((int) index.calculate().asDouble());
        } else {
            throw new RuntimeException("Array expected");
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", variable, index);
    }
}