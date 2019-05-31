package com.interpreter.parser.ast;

import com.interpreter.parser.ast.statements.Statement;
import com.interpreter.parser.variables.Functions;

import java.util.List;

public class FunctionDefine implements Statement {

    private String name;
    private List<String> args;
    private Statement body;

    public FunctionDefine(String name, List<String> args, Statement body) {
        this.name = name;
        this.args = args;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.addFunction(name, new UserFunctionDefine(args, body));
    }
}
