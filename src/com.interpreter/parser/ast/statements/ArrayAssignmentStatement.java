package com.interpreter.parser.ast.statements;

import com.interpreter.parser.ast.expressions.Expression;
import com.interpreter.parser.variables.ArrayValue;
import com.interpreter.parser.variables.Value;
import com.interpreter.parser.variables.Variables;

public class ArrayAssignmentStatement implements Statement {

    private String variable;
    private Expression index;
    private Expression expression;

    public ArrayAssignmentStatement(String variable, Expression index, Expression expression) {
        this.variable = variable;
        this.index = index;
        this.expression = expression;
    }

    @Override
    public void execute() {
        final Value var = Variables.getValue(variable);
        if (var instanceof ArrayValue) {
            final ArrayValue array = (ArrayValue) var;
            array.set((int) index.calculate().asDouble(), expression.calculate());
        } else {
            throw new RuntimeException("Array expected");
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%s] = %s", variable, index, expression);
    }
}
