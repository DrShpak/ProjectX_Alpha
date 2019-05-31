package com.interpreter.parser.ast.expressions;

import com.interpreter.parser.variables.ArrayValue;
import com.interpreter.parser.variables.Value;
import java.util.List;

public class ArrayExpression implements Expression {

    private final List<Expression> elements;

    public ArrayExpression(List<Expression> arguments) {
        this.elements = arguments;
    }

    @Override
    public Value calculate() {
        final int size = elements.size();
        final ArrayValue array = new ArrayValue(size);
        for (int i = 0; i < size; i++) {
            array.set(i, elements.get(i).calculate());
        }
        return array;
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
