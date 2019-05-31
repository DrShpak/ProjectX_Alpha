package com.interpreter.parser.ast.expressions;

import com.interpreter.parser.ast.expressions.Expression;
import com.interpreter.parser.variables.NumberValue;
import com.interpreter.parser.variables.StringValue;
import com.interpreter.parser.variables.Value;

public class ValueExpression implements Expression {

    private Value value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }

    @Override
    public Value calculate() {
        return value;
    }

    @Override
    public String toString() {
        return value.asString();
    }
}