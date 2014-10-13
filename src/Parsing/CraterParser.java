package Parsing;

import CraterExecutionEnvironment.CraterExecutionEnvironmentSingleton;
import Exceptions.CraterParserException;
import ExecutionTree.*;
import NativeDataTypes.CString;
import Scanning.CraterTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

import Scanning.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Created by reidhoruff on 10/7/14.
 */
public class CraterParser {
    private Iterator<Token> tokenIterator;
    private Token currentToken;
    private boolean endTokenStream;
    private Stack<Token> keptTokens;
    private Stack<ETNode> keptETNodes;

    public CraterParser(CraterTokenizer tokenizer) {
        this.tokenIterator = tokenizer.getTokens().iterator();
        this.endTokenStream = false;
        this.keptTokens = new Stack<Token>();
        this.keptETNodes = new Stack<ETNode>();
        this.setCurrentToken();
        this.program();
    }

    private ETNode program() {
        StatementListETNode statements = CraterExecutionEnvironmentSingleton.getInstance().getRootStatementList();

        while(!endTokenStream) {
            statements.add(some(statement()));
        }

        expectEnd();

        return statements;
    }

    private ETNode statement() {
        ETNode node = null;

        if (acceptThenKeepETNode(expression())) {
            node = popKeptETNode();
        } else if (acceptThenKeepETNode(whileStatement())) {
            return popKeptETNode();
        } else if (acceptThenKeepETNode(ifStatement())) {
            return popKeptETNode();
        }

        accept(TokenType.C_SEMICOL);
        return node;
    }

    private ETNode statementList() {
       if (accept(TokenType.C_LCURLEY)) {
           StatementListETNode statements = new StatementListETNode();
           while (acceptThenKeepETNode(statement())) {
               statements.add(popKeptETNode());
           }
           expect(TokenType.C_RCURLEY);
           return statements;
       } else if (acceptThenKeepETNode(statement())) {
           return popKeptETNode();
       } else {
           return null;
       }
    }

    private ETNode whileStatement() {
        if (accept(TokenType.KW_WHILE)) {
            ETNode condition = some(expression());
            ETNode body = some(statementList());
            return new WhileConditionETNode(condition, body);
        } else {
            return null;
        }
    }

    private ETNode ifStatement() {
        if (accept(TokenType.KW_IF)) {
            ETNode condition = some(expression());
            ETNode body = some(statementList());
            ETNode elseBody = null;
            if (accept(TokenType.KW_ELSE)) {
                elseBody = some(statementList());
            }
            return new IfConditionETNode(condition, body, elseBody);
        } else {
            return null;
        }
    }

    private ETNode expression() {

        /**
         * Integer literal
         */
        if (acceptThenKeep(TokenType.R_INT)) {
            IntegerLiteralETNode value = new IntegerLiteralETNode(popKeptToken());
            if (acceptThenKeepExpressionTail(value)) {
                return popKeptETNode();
            }
            return value;
        }

        /**
         * String literal
         */
        if (acceptThenKeep(TokenType.R_STRING_LITERAL)) {
            StringLiteralETNode value = new StringLiteralETNode(popKeptToken());
            if (acceptThenKeepExpressionTail(value)) {
                return popKeptETNode();
            }
            return value;
        }

        /**
         * boolean literal
         */
        if (acceptThenKeep(TokenType.KW_TRUE, TokenType.KW_FALSE)) {
            BooleanLiteralETNode value = new BooleanLiteralETNode(popKeptToken());
            if (acceptThenKeepExpressionTail(value)) {
                return popKeptETNode();
            }
            return value;
        }

        /**
         * identifier reference
         */
        else if (acceptThenKeepETNode(identifierReference())) {
            ETNode identifierReference = popKeptETNode();
            if (acceptThenKeepExpressionTail(identifierReference)) {
                return popKeptETNode();
            }
            return identifierReference;
        }

        /*
        list literal
         */
        else if (acceptThenKeepETNode(list())) {
            ETNode list = popKeptETNode();
            if (acceptThenKeepExpressionTail(list)) {
                return popKeptETNode();
            }
            return list;
        }

        /**
         * none literal
         */
        else if (accept(TokenType.KW_NONE)) {
            ETNode none = new NoneLiteralETNode();
            if (acceptThenKeepExpressionTail(none)) {
                return popKeptETNode();
            }
            return none;
        }

        /**
         * function definition
         */
        else if (acceptThenKeepETNode(functionDefinition())) {
            return popKeptETNode();
        }

        /**
         * (...)
         */
        else if (accept(TokenType.C_LPAREN)) {
            ETNode value = some(expression());
            expect(TokenType.C_RPAREN);
            if (acceptThenKeepExpressionTail(value)) {
                return popKeptETNode();
            }
            return value;
        }

        return null;
    }

    private boolean acceptThenKeepExpressionTail(ETNode expression) {
        if (acceptThenKeepETNode(expressionTail(expression))) {
            return true;
        }
        return false;
    }

    private ETNode expressionTail(ETNode expression) {
        if (acceptThenKeepSimpleOperator()) {
            return new SimpleOperationETNode(expression, popKeptToken(), some(expression()));
        } else if (acceptThenKeepModifyingOperator()) {
            return new IdentifierModifierETNode(expression, popKeptToken(), some(expression()));
        } else if (acceptThenKeepETNode(rangeOperatorTail(expression))) {
            return popKeptETNode();
        } else {
            return null;
        }
    }

    private ETNode rangeOperatorTail(ETNode headExpression) {
        if (accept(TokenType.D_TWO_DOTS)) {
            ETNode tail = some(expression());

            if (accept(TokenType.KW_BY)) {
                ETNode increment = some(expression());
                return new CRangeETNode(headExpression, tail, increment);
            } else {
                return new CRangeETNode(headExpression, tail);
            }
        } else {
            return null;
        }
    }

    private ETNode functionDefinition() {
        if (accept(TokenType.C_PIPE)) {
            ArrayList<String> parameterNames = new ArrayList<String>();
            while (acceptThenKeep(TokenType.R_IDENT)) {
                parameterNames.add(popKeptToken().sequence);
                if (!accept(TokenType.C_COMMA)) break;
            }
            expect(TokenType.C_PIPE);
            expect(TokenType.D_DASH_GT);
            return new FunctionDefinitionETNode(parameterNames, some(statementList()));
        } else {
            return null;
        }
    }

    private ETNode functionCall(ETNode functionReference) {
        if (accept(TokenType.C_LPAREN)) {
            ArrayList<ETNode> parameterExpressions = new ArrayList<ETNode>();
            while (acceptThenKeepETNode(expression())) {
                parameterExpressions.add(popKeptETNode());
                if (!accept(TokenType.C_COMMA)) break;
            }
            expect(TokenType.C_RPAREN);
            ETNode call = new FunctionCallETNode(functionReference, parameterExpressions);
            if (acceptThenKeepETNode(functionCall(call))) {
                return popKeptETNode();
            } else if (acceptThenKeepETNode(listDictReference(call))) {
                return popKeptETNode();
            }
            return call;
        } else {
            return null;
        }

    }

    private ETNode identifierReference() {
        if (acceptThenKeep(TokenType.R_IDENT)) {
            ETNode identifierReference = new IdentifierReferenceETNode(popKeptToken());
            if (acceptThenKeepETNode(listDictReference(identifierReference))) {
                return popKeptETNode();
            } else if (acceptThenKeepETNode(functionCall(identifierReference))) {
                return popKeptETNode();
            }
            return identifierReference;
        } else {
            return null;
        }
    }

    private ETNode listDictReference(ETNode beforeMe) {
       if (accept(TokenType.C_LBRACKSQ)) {
           ETNode indexA = some(expression());
           ETNode indexB = null;
           if (accept(TokenType.D_TWO_DOTS)) {
               indexB = some(expression());
           }
           expect(TokenType.C_RBRACKSQ);

           ETNode baseReference = new ListDictReadETNode(beforeMe, indexA, indexB);

           if (acceptThenKeepETNode(listDictReference(baseReference))) {
               return popKeptETNode();
           } else if (acceptThenKeepETNode(functionCall(baseReference))) {
               return popKeptETNode();
           }
           return baseReference;
       } else {
           return null;
       }
    }

    private ETNode stringLiteral() {
        if (acceptThenKeep(TokenType.R_STRING_LITERAL)) {
            return new StringLiteralETNode(popKeptToken());
        } else {
            return null;
        }
    }

    private ETNode function() {
        if (accept(TokenType.C_LPAREN)) {
            while (acceptThenKeepETNode(identifierReference())) {
            }
            return null;
        } else {
            return null;
        }
    }

    private ETNode list() {
        if (accept(TokenType.C_LBRACKSQ)) {
            ListLiteralETNode list = new ListLiteralETNode();
            if (acceptThenKeepETNode(expression())) {
                list.add(popKeptETNode());
                while (accept(TokenType.C_COMMA) && acceptThenKeepETNode(expression())) {
                    list.add(popKeptETNode());
                }
            }
            expect(TokenType.C_RBRACKSQ);
            return list;
        } else {
            return null;
        }
    }

    private ETNode some(ETNode node) {
        if (node == null) {
            throw new CraterParserException("cannot be empty");
        }
        return node;
    }

    private boolean acceptThenKeepSimpleOperator() {
        return acceptThenKeep(
                TokenType.C_PLUS,
                TokenType.C_DASH,
                TokenType.C_STAR,
                TokenType.C_FLSASH,
                TokenType.KW_AND,
                TokenType.KW_OR,
                TokenType.KW_XOR,
                TokenType.C_LESSTHAN,
                TokenType.D_DOUBLE_EQUALS,
                TokenType.KW_CONTAINS
        );
    }

    private boolean acceptThenKeepModifyingOperator() {
        return acceptThenKeep(
                TokenType.C_EQUALS,
                TokenType.D_PLUS_EQUALS
        );
    }

    /*
        helper functions
     */

    private Token current() {
        return this.currentToken;
    }

    private void setCurrentToken() {
        if (this.tokenIterator.hasNext()) {
            if (this.currentToken != null) {
                System.out.println("consuming: " + this.currentToken.toString());
            }
            this.currentToken = this.tokenIterator.next();
        } else {
            this.endTokenStream = true;
            this.currentToken = null;
        }
    }

    private boolean accept(TokenType... tokens) {
        if (this.currentToken == null) return false;

        for (TokenType token : tokens) {
            if (this.currentToken.token == token) {
                this.setCurrentToken();
                return true;
            }
        }
        return false;
    }

    private boolean sees(TokenType... tokens) {
        if (this.currentToken == null) return false;

        for (TokenType token : tokens) {
            if (this.currentToken.token == token) {
                return true;
            }
        }
        return false;
    }

    private boolean acceptThenKeep(TokenType... tokens) {
        if (sees(tokens)) {
            this.keptTokens.push(this.currentToken);
            this.setCurrentToken();
            return true;
        }
        return false;
    }

    private boolean acceptThenKeepETNode(ETNode node) {
        if (node != null) {
            this.keptETNodes.push(node);
            return true;
        }
        return false;
    }

    private boolean expectThenKeepETNode(ETNode node) {
        if (node != null) {
            this.keptETNodes.push(node);
            return true;
        }
        throw new CraterParserException("I expected something");
    }

    private Token popKeptToken() {
        return this.keptTokens.pop();
    }

    private ETNode popKeptETNode() {
        return this.keptETNodes.pop();
    }

    private boolean expectThenKeep(TokenType... tokens) {
        if (sees(tokens)) {
            this.keptTokens.push(this.currentToken);
            this.setCurrentToken();
            return true;
        }
        // raises exception
        throw new CraterParserException("Unacceptable token: " + this.currentToken.token);
    }

    private Token consume(TokenType... tokens) {
        Token store = this.currentToken;
        expect(tokens);
        return store;
    }

    private boolean expect(TokenType... tokens) {
        if (accept(tokens)) {
            return true;
        }

        throw new CraterParserException("Unacceptable token: " + this.currentToken.token + "(" + this.currentToken.sequence + ") expecting one of: " + Arrays.toString(tokens));
    }

    private boolean expectMoreToken() {
        if (this.endTokenStream) {
            throw new CraterParserException("Unexpected end of stream");
        }
        return true;
    }

    private boolean expectEnd() {
        if (!this.endTokenStream) {
            throw new CraterParserException("Expected End");
        }
        return true;
    }
}
