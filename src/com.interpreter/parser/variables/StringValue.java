package com.interpreter.parser.variables;

public class StringValue implements Value {

    private String value;

    public static final StringValue ZERO = new StringValue("");

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public double asDouble() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public String toString() {
        return asString();
    }
}