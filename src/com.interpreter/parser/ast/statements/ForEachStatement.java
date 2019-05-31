package com.interpreter.parser.ast.statements;

import com.interpreter.parser.ast.expressions.Expression;
import com.interpreter.parser.variables.ArrayValue;
import com.interpreter.parser.variables.Value;

public class ForEachStatement implements Statement {

    private VariableStatement variableStatement;
    private Expression array;
    private Statement block;


    public ForEachStatement(VariableStatement variableStatement, Expression array, Statement block) {
        this.variableStatement = variableStatement;
        this.array = array;
        this.block = block;
    }

    @Override
    public void execute() {
        if (array.calculate() instanceof ArrayValue) {
            ArrayValue arrayValue = (ArrayValue) array.calculate();
            for (Value value : arrayValue.getElements()) {
                variableStatement.setValue(value);
                block.execute();
            }
        } else {
            throw new RuntimeException("Array expression expected! Found (" + array.calculate().asString() + ")");
        }
    }
}
