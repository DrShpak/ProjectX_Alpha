package com.interpreter.parser.variables;

public interface Function {

    Value execute(Value... args);
}