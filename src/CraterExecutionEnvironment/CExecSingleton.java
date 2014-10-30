package CraterExecutionEnvironment;

import BuiltinFunctions.*;
import CraterHelpers.clog;
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
    private CraterVariableScope rootVariableScope;

    public static CExecSingleton get() {
        return ourInstance;
    }

    private CExecSingleton() {
        this.rootStatementList = new StatementListETNode();
        this.statementStack = new Stack<ExecutionStackFrame>();
        this.callStack = new Stack<FunctionCallStackFrame>();
        this.rootVariableScope = new CraterVariableScope();
    }

    public StatementListETNode getRootStatementList() {
        return this.rootStatementList;
    }

    public CraterVariableScope getRootVariableScope() {
        return this.rootVariableScope;
    }

    public void executeProgram() {
        /**
         * extend root variable scope before
         * anything acts on it so that it only contains
         */
        this.rootStatementList.execute(rootVariableScope.extend());
    }

    public Stack<ExecutionStackFrame> getStatementStack() {
        return this.statementStack;
    }

    public Stack<FunctionCallStackFrame> getCallStack() {
        return this.callStack;
    }
}
