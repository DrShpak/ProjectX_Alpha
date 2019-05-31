package com.interpreter.lexer;

import com.interpreter.token.Token;
import com.interpreter.token.TokenType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private static final String ERROR_WRONG_NUMBER = "После числа должен следовать пробел, конец строки или специальный символ.";

    private String input;
    private int length;
    private int currPos = 0;

        private final String WORDS_PATTERN = "^[_A-Za-z][_A-Za-z0-9]*$";
//    private final String WORDS_PATTERN = "[A-Za-z]+";
    private final String END_CHAR = " \0\n\r\t";
    private final String TOKENS_CHAR = "+-*/()=<>!&|!{};,[]";

    private static final Map<String, TokenType> OPERATORS = new HashMap<>();
    static {
        try {
            Files.readAllLines(Paths.get("/Users/test/Documents/Java programmes/ProjectX_GUI/src/com.interpreter/lexer/tokens.txt"))
                    .stream()
                    .filter(line -> line.length() != 0)
                    .forEach(line -> OPERATORS.put(split(line)[0], TokenType.valueOf(split(line)[1])));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] split(String line) {
        return line.split(" ");
    }

    private ArrayList<Token> tokens;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
    }

    public ArrayList<Token> tokenize() {
        tokens = new ArrayList<>();
        char currChar = getCurrChar();
        while (currPos < length) {
            if (TOKENS_CHAR.indexOf(currChar) != -1) {
                tokenizeOperator();
                currChar = getCurrChar();
            } else if (Character.isDigit(currChar)) {
                tokenizeNumber(currChar);
                currChar = getCurrChar();
            } else if (currChar == '"') {
                tokenizeText();
                currChar = getCurrChar();
            }

            else {
                tokenizeWord(currChar);
//                currPos--;
                currChar = getCurrChar();
            }
        }
        return tokens;
    }

    private void tokenizeText() {
        nextChar(); //skip "
        StringBuilder text = new StringBuilder();
        while (getCurrChar() != '"') {
            if (getCurrChar() == '\\') {
                nextChar();
                switch (getCurrChar()) {
                    case '"': text.append('"'); nextChar(); continue;
                    case 'n': text.append('\n'); nextChar(); continue;
                    case 't': text.append('\t'); nextChar(); continue;
                }
                text.append('\\');
                continue;
            }
            text.append(getCurrChar());
            nextChar();
        }
        addToken(TokenType.TEXT, text.toString());
        nextChar();
    }

    /**
     * Метод добавляет соответсвующий токен, если встречено ключевое слово, иначе
     * метод добавляет токен типа "слово", если символ, с которого оно начинается,
     * удовлетворяет шаблону слова. По окончании цикла, из буфера "Word" удаляется последний символ,
     * который к слову уже не относится. Этот же символ остаётся в currChar, который и возвращает метод.
     *
     * @param currChar - символ, с которого начинается слово.
     * @return - следующий за словом символ.
     */
    private void tokenizeWord(char currChar) {
        StringBuilder word = new StringBuilder();
        do {
            word.append(currChar);
            currChar = nextChar();
        } while (word.toString().matches(WORDS_PATTERN));

        word.deleteCharAt(word.length() - 1);
        if (word.toString().equals("if"))
            addToken(TokenType.IF);
        else if (word.toString().equals("else"))
            addToken(TokenType.ELSE);
//        else if (word.toString().equals("print"))
//            addToken(TokenType.PRINT);
        else if (word.toString().equals("while"))
            addToken(TokenType.WHILE);
        else if (word.toString().equals("forEach"))
            addToken(TokenType.FOREACH);
        else if (word.toString().equals("in"))
            addToken(TokenType.IN);
        else if (word.toString().equals("for"))
            addToken(TokenType.FOR);
        else if (word.toString().equals("break"))
            addToken(TokenType.BREAK);
        else if (word.toString().equals("continue"))
            addToken(TokenType.CONTINUE);
        else if (word.toString().equals("def"))
            addToken(TokenType.DEF);
        else if (word.toString().equals("return"))
            addToken(TokenType.RETURN);
        else if (word.length() > 0)
            addToken(TokenType.VARIABLE, word.toString());
        if (word.toString().length() != 0)
            currPos--;
    }

    private void tokenizeNumber(char currChar) {
        StringBuilder buffer = new StringBuilder();
        while (Character.isDigit(currChar)) {
            buffer.append(currChar);
            currChar = nextChar();
        }
//        Если после числа не пробел и не специальный символ, исключение.
//        currChar будет равен 0(\0), если это пустой (null) символ. Такое произойдёт,
//        например, когда данное число - последней символ в коде на нашем языке.
        if (currChar != ' ' && TOKENS_CHAR.indexOf(currChar) == -1 && END_CHAR.indexOf(currChar) == -1)
            throw new RuntimeException(ERROR_WRONG_NUMBER + " (" + buffer + currChar + ")");
        addToken(TokenType.NUMBER, buffer.toString());
    }

    private void tokenizeOperator() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                nextChar();
                nextChar();
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                nextChar();
                nextChar();
                tokenizeMultilineComment();
                return;
            }
        }

        final StringBuilder buffer = new StringBuilder();
        while (true) {
            final String operator = buffer.toString();
            if (!OPERATORS.containsKey(operator + current) && !operator.isEmpty()) {
                addToken(OPERATORS.get(operator));
                return;
            }
            buffer.append(current);
            current = nextChar();
        }
    }

    private void tokenizeMultilineComment() {
        while (true) {
            if (peek(0) == '\0')
                throw new RuntimeException("Missing close tag");
            if (peek(0) == '*' && peek(1) == '/')
                break;
            nextChar();
        }

        nextChar(); // /
        nextChar(); // *
    }

    private void tokenizeComment() {
        while ("\r\n\0".indexOf(getCurrChar()) == -1) {
            nextChar();
        }
    }


    /**
     * взятие текущего символа
     *
     * @return текущий символ
     */
    private char getCurrChar() {
        if (currPos >= length)
            return '\0';
        return input.charAt(currPos);
    }

    private char peek(int relativePos) {
        if (currPos >= length)
            return '\0';
        return input.charAt(currPos + relativePos);
    }

    private char nextChar() {
        currPos++;
        return getCurrChar();
    }

    private void addToken(TokenType type, String data) {
        tokens.add(new Token(type, data));
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }
}