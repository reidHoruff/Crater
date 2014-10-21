package CraterExecutionEnvironment;

import BuiltinFunctions.*;
import CraterHelpers.clog;
import ExecutionTree.ETNode;
import ExecutionTree.StatementListETNode;

import java.util.Stack;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class CExecSingleton {

    private static CExecSingleton ourInstance = new CExecSingleton();
    private Stack<ExecutionStackFrame> statementStack;
    private Stack<FunctionCallStackFrame> callStack;
    private StatementListETNode rootStatementList;

    public static CExecSingleton get() {
        return ourInstance;
    }


    private CExecSingleton() {
        this.rootStatementList = new StatementListETNode();
        this.statementStack = new Stack<ExecutionStackFrame>();
        this.callStack = new Stack<FunctionCallStackFrame>();
    }

    public StatementListETNode getRootStatementList() {
        return this.rootStatementList;
    }

    public void executeProgram() {
        CraterVariableScope rootVariableScope = new CraterVariableScope();
        CExecSingleton.loadBuiltinFunctions(rootVariableScope);
        this.rootStatementList.setVariableScope(rootVariableScope);
        this.rootStatementList.execute();
    }

    public static CraterVariableScope getRootVariableScope() {
        return get().rootStatementList.getVariableScope();
    }

    private static void loadBuiltinFunctions(CraterVariableScope rootScope) {
        clog.m("loading builtin functions...");
        rootScope.nonRecursiveSetValue("len", new LengthBuiltinFunction());
        rootScope.nonRecursiveSetValue("map", new MapBuiltinFunction());
        rootScope.nonRecursiveSetValue("puts", new PrintBuiltinFunction(false));
        rootScope.nonRecursiveSetValue("put", new PrintBuiltinFunction(true));
        rootScope.nonRecursiveSetValue("gets", new GetStringBuiltinFunction());
        rootScope.nonRecursiveSetValue("list", new ListBuiltinFunction());
    }

    public Stack<ExecutionStackFrame> getStatementStack() {
        return this.statementStack;
    }

    public Stack<FunctionCallStackFrame> getCallStack() {
        return this.callStack;
    }
}
