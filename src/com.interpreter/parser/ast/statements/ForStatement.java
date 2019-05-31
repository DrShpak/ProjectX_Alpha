package com.interpreter.parser.ast.statements;

import com.interpreter.parser.ast.expressions.Expression;

public class ForStatement implements Statement {

    private Statement initialization;
    private Expression termination;
    private Statement increment;
    private Statement block;

    public ForStatement(Statement initialization, Expression termination, Statement increment, Statement block) {
        this.initialization = initialization;
        this.termination = termination;
        this.increment = increment;
        this.block = block;
    }

    @Override
    public void execute() {
        for (initialization.execute(); termination.calculate().asDouble() != 0; increment.execute()) {
            try {
                block.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {

            }
        }
    }

    @Override
    public String toString() {
        return "for " + initialization + "; " + termination + "; " + increment + block;
    }
}
