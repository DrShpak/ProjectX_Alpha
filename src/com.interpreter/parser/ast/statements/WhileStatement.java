package com.interpreter.parser.ast.statements;

import com.interpreter.parser.ast.expressions.Expression;

public class WhileStatement implements Statement {

    private Expression condition;
    private Statement statement;

    public WhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {
        while (condition.calculate().asDouble() != 0) {
//            statement.execute();
            try {
                statement.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
//                continue;
            }
        }
    }

    @Override
    public String toString() {
        return "while " + condition + " " + statement;
    }
}