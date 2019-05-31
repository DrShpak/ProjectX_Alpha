package com.interpreter.parser.variables;

import sample.controllers.Controller;
import java.util.HashMap;
import java.util.Scanner;

public class Functions {

    private static final NumberValue ZERO = new NumberValue(0);
    private static HashMap<String, Function> functions = new HashMap<>();

    //тестовые функции
    static {
        functions.put("sin", args -> {
            if (args.length != 1)
                throw new RuntimeException("One argument expected");
            return new NumberValue(Math.sin(args[0].asDouble()));
        });

        functions.put("cos", args -> {
            if (args.length != 1)
                throw new RuntimeException("One argument expected");
            return new NumberValue(Math.cos(args[0].asDouble()));
        });

        functions.put("round", args -> {
            if (args.length != 1)
                throw new RuntimeException("One argument expected");
            return new NumberValue((int) args[0].asDouble());
        });

        functions.put("max", args -> {
            if (args.length != 2)
                throw new RuntimeException("Two argument expected");
            return new NumberValue(Math.max(args[0].asDouble(), args[1].asDouble()));
        });

        functions.put("min", args -> {
            if (args.length != 2)
                throw new RuntimeException("Two argument expected");
            return new NumberValue(Math.min(args[0].asDouble(), args[1].asDouble()));
        });

        functions.put("length", args -> {
            if (args.length != 1)
                throw new RuntimeException("One argument expected");
            return new NumberValue(args[0].asString().length());
        });

        functions.put("isContains", args -> {
            if (args.length != 2)
                throw new RuntimeException("Two argument expected");
            for (char symbol : args[0].asString().toCharArray()) {

            }
            return ZERO;
        });

        functions.put("print", args -> {
            for (Value arg : args) {
//                System.out.print(arg.asString());
                Controller.sb.append(arg.asString());
            }
            return ZERO;
        });

        functions.put("input", args -> {
            Scanner scanner = new Scanner(System.in);
            return new NumberValue(scanner.nextDouble());
        });

        functions.put("newArray", ArrayValue::new);

        functions.put("size", args -> {
            if (args.length != 1)
                throw new RuntimeException("One argument expected");
            if (args[0] instanceof StringValue) {
                return new NumberValue(args[0].asString().length());
            }
            if (args[0] instanceof ArrayValue) {
                ArrayValue array = (ArrayValue) args[0];
                return new NumberValue(array.getSize());
            }
            throw new RuntimeException("Unsupported expression!");
        });
    }

    public static HashMap<String, Function> getFunctions() {
        return functions;
    }

    public static boolean isExists(String funcName) {
        return functions.containsKey(funcName);
    }

    public static Function get(String funcName) {
        if (!isExists(funcName))
            throw new RuntimeException("Unknown function " + funcName);
        return functions.get(funcName);
    }

    public static void addFunction(String varName, Function function) {
        functions.put(varName, function);
    }
}