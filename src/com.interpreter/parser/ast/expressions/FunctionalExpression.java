package com.interpreter.parser.ast.expressions;

import com.interpreter.parser.ast.UserFunctionDefine;
import com.interpreter.parser.ast.expressions.Expression;
import com.interpreter.parser.variables.Function;
import com.interpreter.parser.variables.Functions;
import com.interpreter.parser.variables.Value;
import com.interpreter.parser.variables.Variables;

import java.util.ArrayList;
import java.util.List;

public class FunctionalExpression implements Expression {

    private String name;
    private List<Expression> args;

    public FunctionalExpression(String name, List<Expression> args) {
        this.name = name;
        this.args = args;
    }

    public FunctionalExpression(String name) {
        this.name = name;
        this.args = new ArrayList<>();
    }


    public void addArgs(Expression arg) {
        args.add(arg);
    }

    @Override
    public Value calculate() {
        Value[] values = new Value[args.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = args.get(i).calculate();
        }

        final Function function = Functions.get(name);

        //проверка: является данная функция "родной" или сторонней
        if (function instanceof UserFunctionDefine) {
            final UserFunctionDefine userFunction = (UserFunctionDefine) function;
            if (args.size() != userFunction.getArgsCount()) throw new RuntimeException("Args count mismatch");

            Variables.push(); //кладем все перменные в стек
            for (int i = 0; i < args.size(); i++) {
                //присваиваем нужным переменным нужные значения
                Variables.addVariable(userFunction.getArgsName(i), values[i]);
            }
            final Value result = userFunction.execute(values); // рез-т функции
            Variables.pop(); //восстанавливаем старые значения перемнных (имитация области видимости)
            return result;
        }
        return function.execute(values);
    }
}