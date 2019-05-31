package com.interpreter.parser.ast.statements;

import com.interpreter.parser.variables.Value;
import com.interpreter.parser.variables.Variables;

public class VariableStatement implements Statement {

    private String varName;

    public VariableStatement(String varName) {
        this.varName = varName;
    }


    public void setValue(Value value) {
        Variables.addVariable(varName, value);
    }

    @Override
    public void execute() {
        Variables.addVariable(varName, null);
    }
}
