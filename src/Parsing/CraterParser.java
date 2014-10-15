package Parsing;

import CraterExecutionEnvironment.CraterExecutionEnvironmentSingleton;

import Exceptions.CraterParserException;
import ExecutionTree.*;
import NativeDataTypes.CAtom;
import NativeDataTypes.InfCDT;
import NativeDataTypes.NinfCDT;
import Scanning.CraterTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import Scanning.*;

/**
 * Created by reidhoruff on 10/7/14.
 */
public class CraterParser {
    private Token[] tokenStream;
    private int currentTokenPointer;
    private Stack<Token> keptTokens;
    private Stack<ETNode> keptETNodes;
    private Stack<Integer> streamMarkers;

    public CraterParser(CraterTokenizer tokenizer) {
        this.tokenStream = new Token[tokenizer.getTokens().size()];
        tokenizer.getTokens().toArray(this.tokenStream);
        this.currentTokenPointer = 0;
        this.keptTokens = new Stack<Token>();
        this.keptETNodes = new Stack<ETNode>();
        this.streamMarkers = new Stack<Integer>();
        this.program();
    }

    private ETNode program() {
        StatementListETNode statements = CraterExecutionEnvironmentSingleton.getInstance().getRootStatementList();

        while(this.hasMoreTokens()) {
            statements.add(some(statement()));
        }

        expectEnd();

        return statements;
    }

    private ETNode statement() {
        if (acceptThenKeepETNode(identifierOrListDictAssignmentStatement())) {
            return popKeptETNode();
        } else if (acceptThenKeepETNode(whileStatement())) {
            return popKeptETNode();
        } else if (acceptThenKeepETNode(functionCall())) {
            return popKeptETNode();
        } else if (acceptThenKeepETNode(ifStatement())) {
            return popKeptETNode();
        } else if (acceptThenKeepETNode(forStatement())) {
            return popKeptETNode();
        } else if (acceptThenKeepETNode(modifyingStatement())) {
            return popKeptETNode();
        } else if (accept(TokenType.KW_BREAK)) {
            return new LoopBreakStatementETNode();
        } else if (acceptThenKeepETNode(returnStatement())) {
            return popKeptETNode();
        }

        accept(TokenType.C_SEMICOL);

        return null;
    }

    private ETNode returnStatement() {
        pushTokenStreamMarker();

        if (accept(TokenType.KW_RETURN)) {
            if (acceptThenKeepETNode(expression())) {
                popTokenStreamMarker();
                return new FunctionReturnETNode(popKeptETNode());
            } else {
                popTokenStreamMarker();
                return new FunctionReturnETNode();
            }

        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
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

    private ETNode forStatement() {
        pushTokenStreamMarker();

        if (accept(TokenType.KW_FOR)) {
            Token ident = grabToken(TokenType.R_IDENT);
            expect(TokenType.KW_IN);
            ETNode rangeExpression = some(expression());
            ETNode body = some(statementList());
            popTokenStreamMarker();
            return new ForLoopETNode(ident, rangeExpression, body);
        } else {
            popTokenStreamMarker();
            return null;
        }
    }

    private ETNode expression() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(expressionHead())) {
            ETNode value = popKeptETNode();

            /**
             * something was found... check for a tail...
             */
            if (acceptThenKeepExpressionTail(value)) {
                popTokenStreamMarker();
                return popKeptETNode();
            }

            popTokenStreamMarker();
            return value;
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private ETNode expressionHead() {

        pushTokenStreamMarker();

        ETNode value = null;

        /**
         * Integer literal
         */
        if (acceptThenKeep(TokenType.R_INT)) {
            value = new IntegerLiteralETNode(popKeptToken());
        }

        /**
         * atom
         */
        else if (acceptThenKeep(TokenType.R_ATOM)) {
            value = new CDTLiteralETNode(new CAtom(popKeptToken()));
        }

        /**
         * inf
         */
        else if (accept(TokenType.KW_INF)) {
            value = new CDTLiteralETNode(new InfCDT());
        }

        /**
         * ninf
         */
        else if (accept(TokenType.KW_NINF)) {
            value = new CDTLiteralETNode(new NinfCDT());
        }

        /**
         * String literal
         */
        else if (acceptThenKeep(TokenType.R_STRING_LITERAL)) {
            value = new StringLiteralETNode(popKeptToken());
        }

        /**
         * boolean literal
         */
        else if (acceptThenKeep(TokenType.KW_TRUE, TokenType.KW_FALSE)) {
            value = new BooleanLiteralETNode(popKeptToken());
        }

        /**
         * function call or list dict reference or an identifier
         */
        else if (acceptThenKeepETNode(functionCallOrListDictReferenceOrIdentifier())) {
            value = popKeptETNode();
        }

        /*
        list literal
         */
        else if (acceptThenKeepETNode(list())) {
            value = popKeptETNode();
        }

        /**
         * none literal
         */
        else if (accept(TokenType.KW_NONE)) {
            value = new NoneLiteralETNode();
        }

        /**
         * function definition
         */
        else if (acceptThenKeepETNode(functionDefinition())) {
            popTokenStreamMarker();
            return popKeptETNode();
        }

        /**
         * dictionary
         */
        else if (acceptThenKeepETNode(dict())) {
            popTokenStreamMarker();
            return popKeptETNode();
        }

        /**
         * (...)
         */
        else if (accept(TokenType.C_LPAREN)) {
            value = some(expression());
            expect(TokenType.C_RPAREN);
        }

        /**
         * nothing was found
         */
        else {
            popTokenStreamMarkerAndRestore();
            return null;
        }

        popTokenStreamMarker();
        return value;
    }

    /**
     * anything that could be referenced from an identifier
     * @return
     */
    private ETNode functionCallOrListDictReferenceOrIdentifier() {
        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierReference())) {
            ETNode reference = popKeptETNode();
            while (true) {
                boolean either = false;
                if (acceptThenKeepETNode(functionCallTail(reference))) {
                    reference = popKeptETNode();
                    either = true;
                }

                if (acceptThenKeepETNode(listDictReferenceTail(reference))) {
                    reference = popKeptETNode();
                    either = true;
                }

                if (!either) break;
            }
            popTokenStreamMarker();
            return reference;
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private boolean acceptThenKeepExpressionTail(ETNode expression) {
        if (acceptThenKeepETNode(expressionTail(expression))) {
            return true;
        }
        return false;
    }

    /**
     * {a:b, c:d, ...}
     */
    private ETNode dict() {
        pushTokenStreamMarker();

        if (accept(TokenType.C_LCURLEY)) {
            DictETNode dict = new DictETNode();
            ETNode key, value;

            if (acceptThenKeepETNode(expression())) {
                key = popKeptETNode();

                if (accept(TokenType.C_COLON)) {
                    value = some(expression());
                } else {
                    popTokenStreamMarkerAndRestore();
                    return null;
                }

                dict.put(key, value);

                while (accept(TokenType.C_COMMA)) {
                    key = some(expression());
                    expect(TokenType.C_COLON);
                    value = some(expression());
                    dict.put(key, value);
                }
            }
            expect(TokenType.C_RCURLEY);
            popTokenStreamMarker();
            return dict;
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     *  foo[bar] = 10...
     *  or
     *  foo[bar] = 10...
     */
    private ETNode identifierOrListDictAssignmentStatement() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierOrListDictReference())) {
            ETNode reference = popKeptETNode();
            if (accept(TokenType.C_EQUALS)) {
                popTokenStreamMarker();
                return new IdentifierAssignmentETNode(reference, some(expression()));
            } else {
                popTokenStreamMarkerAndRestore();
                return null;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * foo += 1
     * foo += expression
     * foo()[exp] += exp
     * foo[][][]... += exp
     */
    private ETNode modifyingStatement() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierOrListDictReference())) {
            ETNode reference = popKeptETNode();
            if (acceptThenKeepModifyingOperator()) {
                popTokenStreamMarker();
                return new IdentifierModifierETNode(reference, popKeptToken(), some(expression()));
            } else {
                popTokenStreamMarkerAndRestore();
                return null;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private ETNode expressionTail(ETNode expression) {

        pushTokenStreamMarker();

        ETNode value = null;
        if (acceptThenKeepSimpleOperator()) {
            value = new SimpleOperationETNode(expression, popKeptToken(), some(expressionHead()));
        } else if (acceptThenKeepETNode(rangeOperatorTail(expression))) {
            popTokenStreamMarker();
            return popKeptETNode();
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }

        if (acceptThenKeepExpressionTail(value)) {
            popTokenStreamMarker();
            return popKeptETNode();
        }

        if (value == null) {
            popTokenStreamMarkerAndRestore();
        } else {
            popTokenStreamMarker();
        }

        return value;
    }

    private ETNode rangeOperatorTail(ETNode headExpression) {
        pushTokenStreamMarker();
        if (accept(TokenType.D_TWO_DOTS)) {
            ETNode tail = some(expression());

            if (accept(TokenType.KW_BY)) {
                ETNode increment = some(expression());
                popTokenStreamMarker();
                return new CRangeETNode(headExpression, tail, increment);
            } else {
                popTokenStreamMarker();
                return new CRangeETNode(headExpression, tail);
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private ETNode functionDefinition() {

        pushTokenStreamMarker();

        if (accept(TokenType.C_PIPE)) {
            ArrayList<String> parameterNames = new ArrayList<String>();
            while (acceptThenKeep(TokenType.R_IDENT)) {
                parameterNames.add(popKeptToken().sequence);
                if (!accept(TokenType.C_COMMA)) break;
            }
            expect(TokenType.C_PIPE);
            expect(TokenType.D_DASH_GT);

            if (acceptThenKeepETNode(statementList())) {
                popTokenStreamMarker();
                return new FunctionDefinitionETNode(parameterNames, popKeptETNode());
            } else if (acceptThenKeepETNode(expression())) {
                popTokenStreamMarker();
                return new FunctionDefinitionETNode(parameterNames, popKeptETNode());
            } else {
                popTokenStreamMarkerAndRestore();
                return null;
            }

        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * foo
     * foo[bar]
     * foo()[bar]
     * foo[][]()[][]()[]...
     * .. as long as ends in dict reference
     */
    private ETNode identifierOrListDictReference() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierReference())) {
            ETNode ref = popKeptETNode();

            if (acceptThenKeepETNode(ultimateListDictReferenceTail(ref))) {
                popTokenStreamMarker();
                return popKeptETNode();
            } else {
                popTokenStreamMarker();
                return ref;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * foobar[exp]()[]()
     */
    private ETNode functionCall() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierReference())) {
            ETNode ref = popKeptETNode();
            if (acceptThenKeepETNode(ultimateFunctionCallTail(ref))) {
                popTokenStreamMarker();
                return popKeptETNode();
            } else {
                popTokenStreamMarkerAndRestore();
                return null;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * foobar[exp]()[exp]
     */
    private ETNode listDictReference() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierReference())) {
            ETNode ref = popKeptETNode();
            if (acceptThenKeepETNode(ultimateListDictReferenceTail(ref))) {
                popTokenStreamMarker();
                return popKeptETNode();
            } else {
                popTokenStreamMarkerAndRestore();
                return null;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * foo_bar
     */
    private ETNode identifierReference() {
        if (acceptThenKeep(TokenType.R_IDENT)) {
            return new IdentifierReferenceETNode(popKeptToken());
        } else {
            return null;
        }
    }

    /**
     * can reference dicts and function calls as long as
     * it ends in a function call
     */
    private ETNode ultimateFunctionCallTail(ETNode reference) {

        pushTokenStreamMarker();

        boolean lastWasFunctionTail = false;

        while (acceptThenKeepETNode(listDictReferenceTail(reference))) {
            reference = popKeptETNode();
            lastWasFunctionTail = false;

            if (acceptThenKeepETNode(functionCallTail(reference))) {
                lastWasFunctionTail = true;
                reference = popKeptETNode();
            } else {
                popTokenStreamMarkerAndRestore();
                return null;
            }
        }

        if (lastWasFunctionTail) {
            popTokenStreamMarker();
            return reference;
        } else if (acceptThenKeepETNode(functionCallTail(reference))) {
            popTokenStreamMarker();
            return popKeptETNode();
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * can reference dicts and function calls as long as
     * it ends in a dict/list reference
     */
    private ETNode ultimateListDictReferenceTail(ETNode reference) {

        pushTokenStreamMarker();

        boolean lastWasDictReference = false;

        while (acceptThenKeepETNode(functionCallTail(reference))) {
            reference = popKeptETNode();
            lastWasDictReference = false;

            if (acceptThenKeepETNode(listDictReferenceTail(reference))) {
                lastWasDictReference = true;
                reference = popKeptETNode();
            } else {
                popTokenStreamMarkerAndRestore();
                return null;
            }
        }

        if (lastWasDictReference) {
            popTokenStreamMarker();
            return reference;
        } else if (acceptThenKeepETNode(listDictReferenceTail(reference))) {
            popTokenStreamMarker();
            return popKeptETNode();
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * (exp, exp)()(...)
     */
    private ETNode functionCallTail(ETNode functionReference) {

        pushTokenStreamMarker();

        if (accept(TokenType.C_LPAREN)) {
            ArrayList<ETNode> parameterExpressions = new ArrayList<ETNode>();
            while (acceptThenKeepETNode(expression())) {
                parameterExpressions.add(popKeptETNode());
                if (!accept(TokenType.C_COMMA)) break;
            }
            expect(TokenType.C_RPAREN);
            ETNode call = new FunctionCallETNode(functionReference, parameterExpressions);
            if (acceptThenKeepETNode(functionCallTail(call))) {
                popTokenStreamMarker();
                return popKeptETNode();
            } else {
                popTokenStreamMarker();
                return call;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * [exp][exp]...
     */
    private ETNode listDictReferenceTail(ETNode beforeMe) {

        pushTokenStreamMarker();

        if (accept(TokenType.C_LBRACKSQ)) {
            ETNode index = some(expression());
            expect(TokenType.C_RBRACKSQ);

            ETNode baseReference = new ListDictReadETNode(beforeMe, index);

            if (acceptThenKeepETNode(listDictReferenceTail(baseReference))) {
                popTokenStreamMarker();
                return popKeptETNode();
            } else {
                popTokenStreamMarker();
                return baseReference;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private ETNode list() {
        pushTokenStreamMarker();
        if (accept(TokenType.C_LBRACKSQ)) {
            ListLiteralETNode list = new ListLiteralETNode();
            if (acceptThenKeepETNode(expression())) {
                list.add(popKeptETNode());
                while (accept(TokenType.C_COMMA) && acceptThenKeepETNode(expression())) {
                    list.add(popKeptETNode());
                }
            }
            expect(TokenType.C_RBRACKSQ);
            popTokenStreamMarker();
            return list;
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private ETNode some(ETNode node) {
        if (node == null) {
            throw new CraterParserException("Unexpected: " + getCurrentToken().sequence);
        }
        return node;
    }

    private ETNode some(ETNode node, String msg) {
        if (node == null) {
            throw new CraterParserException("Expected " + msg);
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
                TokenType.C_GREATERTHAN,
                TokenType.D_DOUBLE_EQUALS,
                TokenType.KW_CONTAINS,
                TokenType.C_MOD
        );
    }

    private ETNode atom() {
        if (acceptThenKeep(TokenType.R_ATOM)) {
            return new CDTLiteralETNode(new CAtom(popKeptToken()));
        } else {
            return null;
        }
    }

    private boolean acceptThenKeepModifyingOperator() {
        return acceptThenKeep(
                TokenType.D_PLUS_EQUALS
        );
    }

    private void pushTokenStreamMarker() {
        System.out.println("MARKING: " + this.currentTokenPointer);
        this.streamMarkers.push(this.currentTokenPointer);
    }

    private void popTokenStreamMarker() {
        System.out.println("REMOVING MARK AT: " + this.streamMarkers.pop());
    }

    private void popTokenStreamMarkerAndRestore() {
        int restore = this.currentTokenPointer - streamMarkers.peek();
        if (restore > 0) {
            System.out.println("RESTORING TO: " + streamMarkers.peek());
        } else {
            System.out.println("REMOVING MARK AT: " + this.streamMarkers.peek());
        }
        this.currentTokenPointer = this.streamMarkers.pop();
    }

    /*
        helper functions
     */

    private boolean hasMoreTokens() {
        return this.currentTokenPointer < this.tokenStream.length;
    }

    private Token getCurrentToken() {
        if (hasMoreTokens()) {
            return this.tokenStream[this.currentTokenPointer];
        } else {
            return null;
        }
    }

    private void pushLastToken() {
        if (this.currentTokenPointer == 0) {
            throw new CraterParserException("error");
        }

        this.currentTokenPointer -= 1;
    }

    private void popNextToken() {
        if (hasMoreTokens()) {
            System.out.println("CONSUMED: " + getCurrentToken().sequence);
            this.currentTokenPointer += 1;
        }
    }

    private boolean accept(TokenType... tokens) {
        if (!hasMoreTokens()) {
            return false;
        }

        for (TokenType token : tokens) {
            if (getCurrentToken().token == token) {
                popNextToken();
                return true;
            }
        }

        return false;
    }

    /**
     * doesn't consume any tokens
     */
    private boolean sees(TokenType... tokens) {
        if (!hasMoreTokens()) {
            return false;
        }

        for (TokenType token : tokens) {
            if (getCurrentToken().token == token) {
                return true;
            }
        }
        return false;
    }

    private boolean acceptThenKeep(TokenType... tokens) {
        if (sees(tokens)) {
            this.keptTokens.push(getCurrentToken());
            popNextToken();
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

        throw new CraterParserException("expected node");
    }

    private Token popKeptToken() {
        return this.keptTokens.pop();
    }

    private ETNode popKeptETNode() {
        return this.keptETNodes.pop();
    }

    private boolean expectThenKeep(TokenType... tokens) {
        if (sees(tokens)) {
            this.keptTokens.push(this.getCurrentToken());
            popNextToken();
            return true;
        }

        // raises exception
        throw new CraterParserException("Unacceptable token: " + getCurrentToken().token);
    }

    private boolean expect(TokenType... tokens) {
        if (accept(tokens)) {
            return true;
        }

        throw new CraterParserException("Unacceptable token: " + getCurrentToken().token + "(" + getCurrentToken().sequence + ") expecting one of: " + Arrays.toString(tokens));
    }

    private Token grabToken(TokenType... tokens) {
        for (TokenType tok : tokens) {
            if (tok == getCurrentToken().token) {
                Token found = getCurrentToken();
                popNextToken();
                return found;
            }
        }

        throw new CraterParserException("Unacceptable token: " + getCurrentToken().token + "(" + getCurrentToken().sequence + ") expecting one of: " + Arrays.toString(tokens));
    }

    private boolean expectEnd() {
        if (hasMoreTokens()) {
            throw new CraterParserException("Expected End");
        }

        return true;
    }
}
