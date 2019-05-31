package com.interpreter.parser.variables;

import java.util.HashMap;
//import java.util.Map;
import java.util.Stack;

public class Variables {

    private static final NumberValue ZERO = new NumberValue(0);
    private static HashMap<String, Value> variables = new HashMap<>();

    static {
        variables.put("INT_MIN_VALUE", new NumberValue(Integer.MIN_VALUE));
        variables.put("INT_MAX_VALUE", new NumberValue(Integer.MAX_VALUE));
        variables.put("PI", new NumberValue(Math.PI));
        variables.put("E", new NumberValue(Math.E));
    }

    //стэк для хранения переменных, которые являются аргументами функций
    private static Stack<HashMap<String, Value>> stack = new Stack<>();

    public static HashMap<String, Value> getVariables() {
        return variables;
    }

    //передаеём копию, чтобы сохранить исходные значения
    public static void push() {
        stack.push(new HashMap<>(variables));
    }

    public static void pop() {
        variables = stack.pop();
    }

    public static boolean isExists(String varName) {
        return variables.containsKey(varName);
    }

    public static Value getValue(String varName) {
        if (!isExists(varName))
            return ZERO;
        return variables.get(varName);
    }

    public static void addVariable(String varName, Value value) {
        variables.put(varName, value);
    }
}