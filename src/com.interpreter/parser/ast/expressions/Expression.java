package com.interpreter.parser.ast.expressions;

import com.interpreter.parser.variables.Value;

public interface Expression {

    Value calculate();
}
