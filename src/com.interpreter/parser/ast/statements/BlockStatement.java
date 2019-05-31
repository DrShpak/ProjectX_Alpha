package com.interpreter.parser.ast.statements;

import sample.controllers.Controller;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement {

    private List<Statement> statements;

    public BlockStatement() {
        statements = new ArrayList<>();
    }

    public void add(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
//        statements.forEach(Statement::execute);
        for (Statement statement : statements) {
            statement.execute();
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
//        statements.forEach(statement -> result.append(statements.toString()).append(System.lineSeparator()));
        for (Statement statement : statements) {
            result.append(statement.toString()).append(System.lineSeparator());
        }
        return result.toString();
    }
}