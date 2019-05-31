package com.interpreter.token;

public enum TokenType {

    PLUS,
    MINUS,
    MULTIPLY,
    DIVISION,
    EQUAL,
    VARIABLE,
    ASSIGMENT_OPERATOR,
    LT, // <
    GT, // >
    LE, // <=
    GE, // >=
    NE, // NotEqual !=
    EXCL,
    EOF,

    IF,
    ELSE,

    OPEN_BRACKET, // (
    CLOSE_BRACKET, // )
    LBRACE, // {
    RBRACE, // }
    LBRACKET, // [
    RBRACKET, // ]


    OR,
    OROR,
    AND,
    ANDAND,

    WHILE,
    FOR,
    BREAK,
    CONTINUE,

    DEF,
    RETURN,

    //kew words
    NUMBER,
    TEXT,

    SEPARATOR,
    COMMA,

    FOREACH, // ; для цилка forEach
    IN// ; для цилка for (Python style)
}