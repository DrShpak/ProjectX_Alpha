package com.interpreter.parser.ast.expressions;

import com.interpreter.parser.ast.expressions.Expression;
import com.interpreter.parser.variables.NumberValue;
import com.interpreter.parser.variables.StringValue;
import com.interpreter.parser.variables.Value;

public class ConditionalExpression implements Expression {

    private Operator operation;
    private Expression exp1, exp2;

    public static enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVISION("/"),

        EQUAL("=="), // ==
        LT("<"), // <
        GT(">"), // >
        LE("<="), // <=
        GE(">="), // >=
        NE("!="), // NotEqual !=
        EXCL("!"), // !

        AND("&&"),
        OR("||");

        private final String name;

        Operator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public ConditionalExpression(Operator operation, Expression exp1, Expression exp2) {
        this.operation = operation;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public Value calculate() {
        final Value value1 = exp1.calculate();
        final Value value2 = exp2.calculate();

        double number1, number2;

        if (value1 instanceof StringValue) {
//            final String string1 = value1.asString();
//            final String string2 = value2.asString();
            number1 = value1.asString().compareTo(value2.asString());
            number2 = 0;
            } else {
            number1 = value1.asDouble();
            number2 = value2.asDouble();
        }

        boolean result;
        switch (operation) {
            case EQUAL:
                result = number1 == number2; break;
            case LT:
                result = number1 < number2; break;
            case GT:
                result = number1 > number2; break;
            case LE:
                result = number1 <= number2; break;
            case GE:
                result = number1 >= number2; break;
            case NE:
                result = number1 != number2; break;
            case AND:
                result = (number1 != 0) && (number2 != 0); break;
            case OR:
                result = (number1 != 0) || (number2 != 0); break;
            default:
                throw new RuntimeException("Неизвестная операция");
        }
        return new NumberValue(result);
    }

    @Override
    public String toString() {
        return exp1 + " " + operation.getName() + " " + exp2;
    }
}