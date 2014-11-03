package Parsing;

import CraterExecutionEnvironment.CExecSingleton;

import CraterHelpers.clog;
import Exceptions.CraterInternalException;
import Exceptions.CraterParserException;
import ExecutionTree.*;
import NativeDataTypes.CAtom;
import NativeDataTypes.InfCDT;
import NativeDataTypes.NinfCDT;
import Scanning.CraterTokenizer;

import java.util.ArrayList;
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
    private Stack<Integer> streamMarkerStack;
    private int insideClass, insideFunction, insideStatic;

    public CraterParser(CraterTokenizer tokenizer) {
        this.insideClass = 0;
        this.insideFunction = 0;
        this.insideStatic = 0;

        this.tokenStream = new Token[tokenizer.getTokens().size()];
        tokenizer.getTokens().toArray(this.tokenStream);
        this.currentTokenPointer = 0;
        this.keptTokens = new Stack<Token>();
        this.keptETNodes = new Stack<ETNode>();
        this.streamMarkerStack = new Stack<Integer>();
        this.program();
    }

    private ETNode program() {
        StatementListETNode statements = CExecSingleton.get().getRootStatementList();

        while(this.hasMoreTokens()) {
            statements.add(some(statement()));
        }

        expectEnd();

        return statements;
    }

    private ETNode statement() {

        pushTokenStreamMarker();

        ETNode statement = null;

        if (acceptThenKeepETNode(functionCall())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(whileStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(functionDefinitionStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(ifStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(forStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(modifyingStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(breakStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(returnStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(variableDefinitionStatement())) {
            statement = popKeptETNode();
        }

        else if (acceptThenKeepETNode(classDefinitionStatement())) {
            statement = popKeptETNode();
        }

        else {
            popTokenStreamMarkerAndRestore();
            return null;
        }

        while(accept(TokenType.C_SEMICOL));

        statement.setSpawningToken(peekMarkedToken());

        popTokenStreamMarker();

        return statement;
    }

    /**
     * class foobar [:: baz] {
     *
     * }
     *
     */
    private ETNode classDefinitionStatement() {

        pushTokenStreamMarker();

        if (accept(TokenType.KW_CLASS)) {
            Token name = grabToken(TokenType.R_IDENT);

            /**
            if (accept(TokenType.D_DBL_COLON)) {
                Token baseIdent = grabToken(TokenType.R_IDENT);
            }
             */

            expect(TokenType.C_LCURLEY);

            enterClass();

            ClassDefinitionETNode classBody = new ClassDefinitionETNode(name.sequence);

            while (true) {
                if (acceptThenKeepETNode(classConstructorStatement())) {
                    classBody.getCClass().setConstructorDefinition(popKeptETNode());
                    continue;
                }

                if (acceptThenKeepETNode(variableDefinitionStatement())) {
                    classBody.getCClass().addVariable(popKeptETNode());
                    continue;
                }

                if (acceptThenKeepETNode(functionDefinitionStatement())) {
                    classBody.getCClass().addFunction(popKeptETNode());
                    continue;
                }


                if (acceptThenKeepETNode(staticFunctionDefinitionStatement())) {
                    classBody.getCClass().addStaticFunction(popKeptETNode());
                    continue;
                }

                if (acceptThenKeepETNode(privateStaticFunctionDefinitionStatement())) {
                    classBody.getCClass().addStaticPrivateFunction(popKeptETNode());
                    continue;
                }
                break;
            }

            expect(TokenType.C_RCURLEY);
            exitClass();

            popTokenStreamMarker();
            return new IdentifierDefinitionETNode(name, classBody, true);
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private ETNode objectInstantiation() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(functionCallOrListDictReferenceOrIdentifierOrMember())) {

        }

        return null;
    }

    /**
     * init || -> {}
     */
    private ETNode classConstructorStatement() {

        pushTokenStreamMarker();

        if (accept(TokenType.KW_NEW)) {
            return some(functionDefinition(), "class constructor");
        } else {
            popTokenStreamMarker();
            return null;
        }
    }

    /**
     * fun foo || -> {}
     */
    private ETNode functionDefinitionStatement() {

        pushTokenStreamMarker();

        if (accept(TokenType.KW_FUN)) {
            Token name = grabToken(TokenType.R_IDENT);
            ETNode functionDefinition = some(functionDefinition(), "function definition");
            return new IdentifierDefinitionETNode(name, functionDefinition, true);
        } else {
            popTokenStreamMarker();
            return null;
        }
    }

    /**
     * stat fun foo || -> {}
     */
    private ETNode staticFunctionDefinitionStatement() {

        pushTokenStreamMarker();

        if (accept(TokenType.KW_STATIC)) {

            enterStatic();

            if(acceptThenKeepETNode(functionDefinitionStatement())) {

                exitStatic();

                popTokenStreamMarker();
                return popKeptETNode();
            } else {

                exitStatic();

                popTokenStreamMarkerAndRestore();
                return null;
            }
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * priv stat fun foo || -> {}
     */
    private ETNode privateStaticFunctionDefinitionStatement() {

        pushTokenStreamMarker();

        if (accept(TokenType.KW_PRIVATE) && acceptThenKeepETNode(staticFunctionDefinitionStatement())) {
            popTokenStreamMarker();
            return popKeptETNode();
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * int, bool, list...
     */
    private boolean acceptThenKeepType() {
        return acceptThenKeep(
                TokenType.KW_INT,
                TokenType.KW_BOOL,
                TokenType.KW_LIST
        );
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

    private ETNode breakStatement() {
        if (accept(TokenType.KW_BREAK)) {
            return new LoopBreakStatementETNode();
        } else {
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

    private ETNode statementListOrExpression() {
        pushTokenStreamMarker();

        if (acceptThenKeepETNode(statementList()) || acceptThenKeepETNode(expression())) {
            popTokenStreamMarker();
            return popKeptETNode();
        } else {
            popTokenStreamMarkerAndRestore();
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
            ETNode body = some(statementListOrExpression());
            ETNode elseBody = null;
            if (accept(TokenType.KW_ELSE)) {
                elseBody = some(statementListOrExpression());
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
            if (acceptThenKeepETNode(expressionTail(value))) {
                value = popKeptETNode();
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
            value.setSpawningToken(peekMarkedToken());
        }

        /**
         * atom
         */
        else if (acceptThenKeep(TokenType.R_ATOM)) {
            value = new CDTLiteralETNode(new CAtom(popKeptToken()));
            value.setSpawningToken(peekMarkedToken());
        }

        /**
         * inf
         */
        else if (accept(TokenType.KW_INF)) {
            value = new CDTLiteralETNode(new InfCDT());
            value.setSpawningToken(peekMarkedToken());
        }

        /**
         * ninf
         */
        else if (accept(TokenType.KW_NINF)) {
            value = new CDTLiteralETNode(new NinfCDT());
            value.setSpawningToken(peekMarkedToken());
        }

        /**
         * String literal
         */
        else if (acceptThenKeep(TokenType.R_STRING_LITERAL)) {
            value = new StringLiteralETNode(popKeptToken());
            value.setSpawningToken(peekMarkedToken());
        }

        /**
         * boolean literal
         */
        else if (acceptThenKeep(TokenType.KW_TRUE, TokenType.KW_FALSE)) {
            value = new BooleanLiteralETNode(popKeptToken());
            value.setSpawningToken(peekMarkedToken());
        }

        /**
         * function call or list dict reference or an identifier
         */
        else if (acceptThenKeepETNode(functionCallOrListDictReferenceOrIdentifierOrMember())) {
            value = popKeptETNode();
        }

        /*
        list literal
         */
        else if (acceptThenKeepETNode(list())) {
            value = popKeptETNode();
        }

        /*
        tuple literal
         */
        else if (acceptThenKeepETNode(tuple())) {
            value = popKeptETNode();
        }

        /**
         * none literal
         */
        else if (accept(TokenType.KW_NONE)) {
            //System.out.println("NONE");
            value = new NoneLiteralETNode();
            value.setSpawningToken(peekMarkedToken());
        }

        /**
         * function definition
         */
        else if (acceptThenKeepETNode(functionDefinition())) {
            value = popKeptETNode();
        }

        /**
         * dictionary
         */
        else if (acceptThenKeepETNode(dict())) {
            value = popKeptETNode();
        }

        /**
         * not
         */
        else if (accept(TokenType.KW_NOT)) {
            value = new NotETNode(some(expression()));
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
    private ETNode functionCallOrListDictReferenceOrIdentifierOrMember() {
        pushTokenStreamMarker();
        if (acceptThenKeepETNode(identifierReference())) {
            ETNode reference = popKeptETNode();
            if (acceptThenKeepETNode(callOrListDictReferenceTailOrMember(reference))) {
                reference = popKeptETNode();
            }
            popTokenStreamMarker();
            return reference;
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * [exp]
     * ()
     * ()[]
     * [].bar()
     */
    private ETNode callOrListDictReferenceTailOrMember(ETNode reference) {
        pushTokenStreamMarker();
        boolean any = false;
        while (true) {
            boolean either = false;
            if (acceptThenKeepETNode(functionCallTail(reference))) {
                reference = popKeptETNode();
                either = true;
                any = true;
            }

            if (acceptThenKeepETNode(listDictReferenceTail(reference))) {
                reference = popKeptETNode();
                either = true;
                any = true;
            }

            if (acceptThenKeepETNode(memberAccessTail(reference))) {
                reference = popKeptETNode();
                either = true;
                any = true;
            }

            if (!either) break;
        }

        if (any) {
            popTokenStreamMarker();
            return reference;
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
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
     * var x = ...
     */
    private ETNode variableDefinitionStatement() {
        pushTokenStreamMarker();

        if (accept(TokenType.KW_VAR) && expectThenKeep(TokenType.R_IDENT)) {
            Token ident = popKeptToken();
            expect(TokenType.C_EQUALS);
            ETNode exp = some(expression());
            popTokenStreamMarker();
            return new IdentifierDefinitionETNode(ident, exp, false);
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * foo += 1
     * foo += expression
     * foo[exp] += exp
     * foo[][][]... += exp
     */
    private ETNode modifyingStatement() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierOrListDictReference())) {
            ETNode reference = popKeptETNode();
            reference.setSpawningToken(peekMarkedToken());

            if (acceptThenKeepModifyingOperator()) {
                Token operator = popKeptToken();
                ETNode modifier = new IdentifierModifierETNode(reference, operator.token, some(expression()));
                modifier.setSpawningToken(operator);
                popTokenStreamMarker();
                return modifier;
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
            Token operator = popKeptToken();
            boolean not = (operator.token == TokenType.KW_IS && accept(TokenType.KW_NOT));
            value = new SimpleOperationETNode(expression, operator, some(expressionHead()));
            value.setSpawningToken(peekMarkedToken());

            /* 'is not' */
            if (not) {
                value = new NotETNode(value);
            }
        }

        else if (acceptThenKeepETNode(rangeOperatorTail(expression))) {
            value = popKeptETNode();
        }

        else if (acceptThenKeepETNode(callOrListDictReferenceTailOrMember(expression))) {
            value = popKeptETNode();
        }

        /**
         * piecewise
         */
        else if (accept(TokenType.C_QUESTION)) {
            ETNode a = some(expression());
            expect(TokenType.KW_ELSE);
            ETNode b = some(expression());
            return new IfConditionETNode(expression, a, b);
        }

        else {
            popTokenStreamMarkerAndRestore();
            return null;
        }

        if (acceptThenKeepETNode(expressionTail(value))) {
            value = popKeptETNode();
        }

        if (value == null) {
            popTokenStreamMarkerAndRestore();
            return null;
        } else {
            popTokenStreamMarker();
            return value;
        }
    }

    /**
     * ..(exp)[by exp]
     */
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

    /**
     * || -> {exp}
     */
    private ETNode functionDefinition() {

        pushTokenStreamMarker();

        ArrayList<String> parameterNames = new ArrayList<String>();

        if (accept(TokenType.C_PIPE)) {
            while (acceptThenKeep(TokenType.R_IDENT)) {
                parameterNames.add(popKeptToken().sequence);
                if (!accept(TokenType.C_COMMA)) break;
            }
            expect(TokenType.C_PIPE);
            expect(TokenType.D_DASH_GT);

            /**
             * entering function body
             */
            enterFunction();

            if (acceptThenKeepETNode(statementListOrExpression())) {
                exitFunction();
                popTokenStreamMarker();
                return new FunctionDefinitionETNode(parameterNames, popKeptETNode());
            } else {
                exitFunction();
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
     * foo[][][].foo.bar[][]...
     * .. only []
     */
    private ETNode identifierOrListDictReference() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierReference())) {
            ETNode ref = popKeptETNode();
            ref.setSpawningToken(peekMarkedToken());

            if (acceptThenKeepETNode(listDictMemberTail(ref))) {
                ref = popKeptETNode();
            }

            popTokenStreamMarker();
            return ref;

        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    /**
     * foobar[exp].baz()[]()
     * 2()
     * 2.foo()
     */
    private ETNode functionCall() {

        pushTokenStreamMarker();

        if (acceptThenKeepETNode(identifierReference()) || acceptThenKeepETNode(expressionHead())) {
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
     * foo_bar
     */
    private ETNode identifierReference() {
        /**
         * static and this are key words but are also used referenced as variables at times... (within class[static]
         * functions)
         */
        if (acceptThenKeep(TokenType.R_IDENT)) {
            return new IdentifierReferenceETNode(popKeptToken());
        }

        if (isInsideClass() && isInsideFunction()) {
            if (isInsideStatic() && acceptThenKeep(TokenType.KW_STATIC)) {
                return new IdentifierReferenceETNode(popKeptToken());
            }

            if (!isInsideStatic() && acceptThenKeep(TokenType.KW_STATIC, TokenType.KW_THIS)) {
                return new IdentifierReferenceETNode(popKeptToken());
            }

        }

        return null;
    }

    /**
     * can reference dicts, members, and function calls as long as
     * it ends in a function call
     */
    private ETNode ultimateFunctionCallTail(ETNode reference) {

        pushTokenStreamMarker();
        boolean lastWasFunctionTail = false;

        while (true) {
            if (acceptThenKeepETNode(functionCallTail(reference))) {
                reference = popKeptETNode();
                lastWasFunctionTail = true;
                continue;
            }

            if (acceptThenKeepETNode(listDictMemberTail(reference))) {
                reference = popKeptETNode();
                lastWasFunctionTail = false;
                continue;
            }

            break;
        }

        if (lastWasFunctionTail) {
            popTokenStreamMarker();
            return reference;
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
     *  .foo
     *  [exp]
     *  .foo[exp].foo[][]
     */
    private ETNode listDictMemberTail(ETNode beforeMe) {
        pushTokenStreamMarker();

        ETNode reference = beforeMe;
        boolean any = false;

        while (true) {
            if (acceptThenKeepETNode(listDictReferenceTail(reference))) {
                reference = popKeptETNode();
                any = true;
                continue;
            }

            if (acceptThenKeepETNode(memberAccessTail(reference))) {
                reference = popKeptETNode();
                any = true;
                continue;
            }

            break;
        }

        if (any) {
            popTokenStreamMarker();
            return reference;
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

    /**
     * .foo
     * .bar.baz...
     */
    private ETNode memberAccessTail(ETNode beforeMe) {

        pushTokenStreamMarker();

        ETNode reference = beforeMe;
        boolean found = false;

        while (accept(TokenType.C_PERIOD) && acceptThenKeep(TokenType.R_IDENT, TokenType.KW_NEW)) {
            Token name = popKeptToken();
            reference = new MemberAccessorETNode(reference, name);
            found = true;
        }

        if (found) {
            popTokenStreamMarker();
            return reference;
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

    private ETNode tuple() {
        pushTokenStreamMarker();
        if (accept(TokenType.C_LPAREN)) {
            TupleLiteralETNode tuple = new TupleLiteralETNode();
            boolean hasAtLeastTwo = false;
            if (acceptThenKeepETNode(expression())) {
                tuple.add(popKeptETNode());
                while (accept(TokenType.C_COMMA) && acceptThenKeepETNode(expression())) {
                    tuple.add(popKeptETNode());
                    hasAtLeastTwo = true;
                }
            }

            if (!hasAtLeastTwo) {
                popTokenStreamMarkerAndRestore();
                return null;
            }

            expect(TokenType.C_RPAREN);
            popTokenStreamMarker();
            return tuple;
        } else {
            popTokenStreamMarkerAndRestore();
            return null;
        }
    }

    private ETNode some(ETNode node) {
        if (node == null) {
            throw new CraterParserException("Unexpected: " + getCurrentToken().toString());
        }
        return node;
    }

    private ETNode some(ETNode node, String msg) {
        if (node == null) {
            throw new CraterParserException("Expected " + msg + " not " + getCurrentToken());
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
                TokenType.C_MOD,
                TokenType.KW_IS,
                TokenType.D_NOT_EQUAL
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
                TokenType.D_PLUS_EQUALS,
                TokenType.C_EQUALS
        );
    }

    private Token peekMarkedToken() {
        return this.tokenStream[this.streamMarkerStack.peek()];
    }

    private void pushTokenStreamMarker() {
        clog.m("MARKING: " + this.currentTokenPointer);
        this.streamMarkerStack.push(this.currentTokenPointer);
    }

    private Token popTokenStreamMarker() {
        Token tok = peekMarkedToken();
        clog.m("REMOVING MARK AT: " + this.streamMarkerStack.pop());
        return tok;
    }

    private void popTokenStreamMarkerAndRestore() {
        int restore = this.currentTokenPointer - streamMarkerStack.peek();
        if (restore > 0) {
            clog.m("RESTORING TO: " + streamMarkerStack.peek());
        } else {
            clog.m("REMOVING MARK AT: " + this.streamMarkerStack.peek());
        }
        this.currentTokenPointer = this.streamMarkerStack.pop();
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

    private void popNextToken() {
        if (hasMoreTokens()) {
            clog.m("CONSUMED: " + getCurrentToken().sequence);
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
        throw new CraterParserException("Unacceptable token: " + getCurrentToken().toString());
    }

    private boolean expect(TokenType... tokens) {
        if (accept(tokens)) {
            return true;
        }

        if (!hasMoreTokens()) {
            throw new CraterParserException("Unexpected end of token stream");
        }

        throw new CraterParserException("Unexpected:\n" + getCurrentToken().toString());
    }

    private Token grabToken(TokenType... tokens) {
        for (TokenType tok : tokens) {
            if (tok == getCurrentToken().token) {
                Token found = getCurrentToken();
                popNextToken();
                return found;
            }
        }

        throw new CraterParserException("Unexpected:\n" + getCurrentToken().toString());
    }

    private boolean expectEnd() {
        if (hasMoreTokens()) {
            throw new CraterParserException("Expected End");
        }

        return true;
    }

    private void enterFunction() {
        this.insideFunction += 1;
    }

    private void exitFunction() {
        this.insideFunction -= 1;

        if (this.insideFunction < 0) {
            throw new CraterInternalException("insideFunction < 0");
        }
    }

    private boolean isInsideFunction() {
        return this.insideFunction > 0;
    }

    private void enterClass() {
        this.insideClass += 1;
    }

    private void exitClass() {
        this.insideClass -= 1;

        if (this.insideClass < 0) {
            throw new CraterInternalException("insideClass < 0");
        }
    }

    private boolean isInsideClass() {
        return this.insideClass > 0;
    }

    private void enterStatic() {
        this.insideStatic += 1;
    }

    private void exitStatic() {
        this.insideStatic -= 1;

        if (this.insideStatic < 0) {
            throw new CraterInternalException("insideStatic < 0");
        }
    }

    private boolean isInsideStatic() {
        return this.insideStatic > 0;
    }
}
