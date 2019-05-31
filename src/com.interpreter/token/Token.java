package com.interpreter.token;

public class Token {

    private TokenType type;
    private String data;

    public Token() {}

    public Token(TokenType type, String data) {
        this.type = type;
        this.data = data;
    }

    public Token(TokenType type) {
        this(type, null);
    }

    public TokenType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return type + " " + data;
    }
}
