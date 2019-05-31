package com.interpreter.parser;

import com.interpreter.parser.ast.*;
import com.interpreter.parser.ast.expressions.*;
import com.interpreter.parser.ast.statements.*;
import com.interpreter.token.Token;
import com.interpreter.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final Token EOF = new Token(TokenType.EOF);
    private ArrayList<Token> tokens;
    private int currPos;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public Statement parse() {
        BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            result.add(statement());
    }
        return result;
    }

    private Statement block() {
        BlockStatement block = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            block.add(statement());
        }
        return block;
    }

    private Statement statementOrBlock() {
        if (getCurrToken().getType() == TokenType.LBRACE)
            return block();
        return statement();
    }

    private Statement statement() {
        if (match(TokenType.IF))
            return ifElse();
        if (match(TokenType.WHILE))
            return whileStatement();
        if (match(TokenType.FOREACH))
            return forEachStatement();
        if (match(TokenType.FOR))
            return forStatement();
        if (match(TokenType.BREAK))
            return new BreakStatement();
        if (match(TokenType.CONTINUE))
            return new ContinueStatement();
        if (match(TokenType.RETURN))
            return new ReturnStatement(expression());
        if (match(TokenType.DEF))
            return functionDefine();
        if (getCurrToken(0).getType() == TokenType.VARIABLE && getCurrToken(1).getType() == TokenType.OPEN_BRACKET) {
            return new FunctionStatement(function());
        }
        return assignmentStatement();
    }

    /**
     * Метод определения оператора присваивания.
     * Созданный оператор сразу "извлекается", помещая переменную в базу данных.
     * Если набор токенов не соответсвует оператору, исключение.
     *
     * @return оператор, включающий в себя результат своей работы.
     */
    /*private Statement assignmentStatement() {
        Token firstOperand = getCurrToken();
        currPos++;
        if (firstOperand.getType() == TokenType.VARIABLE && match(TokenType.ASSIGMENT_OPERATOR)) {
            return new AssignmentStatement(firstOperand.getData(), expression());
        }
        throw new RuntimeException("Unknown statement");
    }*/

    private Statement assignmentStatement() {

        if (lookMatch(0, TokenType.VARIABLE) && lookMatch(1, TokenType.ASSIGMENT_OPERATOR)) {
            final String variable = consume(TokenType.VARIABLE).getData();
            consume(TokenType.ASSIGMENT_OPERATOR);
            return new AssignmentStatement(variable, expression());
        }
        if (lookMatch(0, TokenType.VARIABLE) && lookMatch(1, TokenType.LBRACKET)) {
            final String variable = consume(TokenType.VARIABLE).getData();
            consume(TokenType.LBRACKET);
            final Expression index = expression();
            consume(TokenType.RBRACKET);
            consume(TokenType.ASSIGMENT_OPERATOR);
            return new ArrayAssignmentStatement(variable, index, expression());
        }

        throw new RuntimeException("Unknown statement");
    }

    /**
     * Метод определения условного оператора.
     * Сначала рассчитывается выражение условия, затем создаётся тело if,
     * после, если за телом if следует токен типа "ELSE", формируется тело блока else,
     * иначе ему присваевается null и оно исполняться не будет.
     * @return оператор, включающий в себя результат своей работы.
     */

    private Statement ifElse() {
        Expression condition = expression();
        Statement ifStatement = statementOrBlock();
        Statement elseStatement = match(TokenType.ELSE) ? statementOrBlock() : null;
        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Statement whileStatement() {
        Expression condition = expression();
        Statement statement = statementOrBlock();
        return new WhileStatement(condition, statement);
    }

    private Statement forStatement() {
        Statement initialization = assignmentStatement();
        consume(TokenType.SEPARATOR);
        Expression termination = expression();
        consume(TokenType.SEPARATOR);
        Statement increment = assignmentStatement();
        Statement statement = statementOrBlock();
        return new ForStatement(initialization, termination, increment, statement);
    }

    private Statement forEachStatement() {
        String varName = consume(TokenType.VARIABLE).getData();
        VariableStatement varStatement = new VariableStatement(varName);
        consume(TokenType.IN);
        Expression arrayExpression = expression();
        Statement statement = statementOrBlock();
        return new ForEachStatement(varStatement, arrayExpression, statement);
    }

    private FunctionDefine functionDefine() {
        String name = consume(TokenType.VARIABLE).getData();
        consume(TokenType.OPEN_BRACKET);
        List<String> args = new ArrayList<>();
        while (!match(TokenType.CLOSE_BRACKET)) {
            args.add(consume(TokenType.VARIABLE).getData());
            match(TokenType.COMMA);
        }

        Statement body = statementOrBlock();
        return new FunctionDefine(name, args, body);
    }

    private FunctionalExpression function() {
        String name = consume(TokenType.VARIABLE).getData();
        consume(TokenType.OPEN_BRACKET);
        FunctionalExpression function = new FunctionalExpression(name);
        while (!match(TokenType.CLOSE_BRACKET)) {
            function.addArgs(expression());
            match(TokenType.COMMA);
        }
        return function;
    }

    private Expression array() {
        consume(TokenType.LBRACKET);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }

    private Expression element() {
        final String variable = consume(TokenType.VARIABLE).getData();
        consume(TokenType.LBRACKET);
        final Expression index = expression();
        consume(TokenType.RBRACKET);
        return new ArrayAccessExpression(variable, index);
    }

    private Expression expression() {
        return logicalOr();
    }

    private Expression logicalOr() {
        Expression result = logicalAnd();

        while (true) {
            if (match(TokenType.OROR)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression logicalAnd() {
        Expression result = equality();

        while (true) {
            if (match(TokenType.ANDAND)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, equality());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression equality() {
        Expression result = conditional();

        if (match(TokenType.EQUAL)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUAL, result, conditional());
        }

        if (match(TokenType.NE)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NE, result, conditional());
        }

        return result;
    }

    /**
     * Метод создаёт выражение, содержащее некоторое условие.
     * Приоритет его расчёта ниже, чем у операции сложения.
     *
     * @return условный оператор.
     */
    private Expression conditional() {
        Expression result = additive();

        boolean isTrue = true;
        while (isTrue) {
            TokenType type = getCurrToken().getType();
            switch (type) {
                case LT:
                    nextToken();
                    result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, additive());
                    break;
                case GT:
                    nextToken();
                    result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, additive());
                    break;
                case LE:
                    nextToken();
                    result = new ConditionalExpression(ConditionalExpression.Operator.LE, result, additive());
                    break;
                case GE:
                    nextToken();
                    result = new ConditionalExpression(ConditionalExpression.Operator.GE, result, additive());
                    break;
                default:
                    isTrue = false;
                    break;
            }
        }
        return result;
    }

    private Expression additive() {
        Expression result = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression('+', result, multiplicative());
                continue;
            }
            if (match(TokenType.MINUS)) {
                result = new BinaryExpression('-', result, multiplicative());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();

        while (true) {
            if (match(TokenType.MULTIPLY)) {
                result = new BinaryExpression('*', result, unary());
                continue;
            }
            if (match(TokenType.DIVISION)) {
                result = new BinaryExpression('/', result, unary());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression('-', primary());
        }
        return primary();
    }

    //парсим числа, строки и тд
    //самый нижний уровень в рекурсивном спуске
    private Expression primary() {
        Token current = getCurrToken();
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getData()));
        }

        if (getCurrToken(0).getType() == TokenType.VARIABLE && getCurrToken(1).getType() == TokenType.OPEN_BRACKET) {
            return function();
        }

        if (lookMatch(0, TokenType.VARIABLE) && lookMatch(1, TokenType.LBRACKET)) {
            return element();
        }
        if (lookMatch(0, TokenType.LBRACKET)) {
            return array();
        }

        if (match(TokenType.VARIABLE)) {
            return new VariableExpression(current.getData());
        }

        if (match(TokenType.TEXT)) {
            return new ValueExpression(current.getData());
        }

        if (match(TokenType.OPEN_BRACKET)) {
            Expression result = expression();
            match(TokenType.CLOSE_BRACKET);
            return result;
        }
        System.out.println("ПЛОХОЙ ТОКЕН = " + current + " position " + currPos);
        throw new RuntimeException("Unknown expression");
    }

    private Token consume(TokenType type) {
        Token current = getCurrToken();
        if (type != current.getType()) throw new RuntimeException("Token " + current + " doesn't match " + type);
        currPos++;
        return current;
    }

    private boolean lookMatch(int pos, TokenType type) {
        return getCurrToken(pos).getType() == type;
    }

    /**
     * метод для взятия текущего токена
     * в случае, если вышли за границы списка токенов - выбрасывается исключение
     *
     * @return текущий токен
     */
    private Token getCurrToken() {
        if (currPos >= tokens.size())
            return EOF;
        return tokens.get(currPos);
    }

    private Token getCurrToken(int relativePos) {
        if (currPos + relativePos >= tokens.size())
            return EOF;
        return tokens.get(currPos + relativePos);
    }

    private void nextToken() {
        currPos++;
    }

    private boolean match(TokenType type) {
        Token currToken = getCurrToken();
        if (type != currToken.getType()) {
            return false;
        }
        currPos++;
        return true;
    }
}