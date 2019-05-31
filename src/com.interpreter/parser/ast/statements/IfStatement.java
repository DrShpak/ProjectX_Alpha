package com.interpreter.parser.ast.statements;

import com.interpreter.parser.ast.expressions.Expression;

public class IfStatement implements Statement {

    private Expression conditional;
    private Statement ifStatement, elseStatement;

    public IfStatement(Expression conditional, Statement ifStatement, Statement elseStatement) {
        this.conditional = conditional;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        double result = conditional.calculate().asDouble();
        if (result != 0) {
            ifStatement.execute();
        } else if (elseStatement != null) {
            elseStatement.execute();
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("if ").append(conditional).append(" ").append(ifStatement);
        if (elseStatement != null)
            buffer.append("\nelse ").append(elseStatement);
        return buffer.toString();
    }
}
