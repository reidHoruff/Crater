package CraterExecutionEnvironment;

import BuiltinFunctions.*;
import ExecutionTree.ETNode;
import ExecutionTree.StatementListETNode;
import NativeDataTypes.CDT;

import java.util.HashMap;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class CraterExecutionEnvironmentSingleton {
    private static CraterExecutionEnvironmentSingleton ourInstance = new CraterExecutionEnvironmentSingleton();

    public static CraterExecutionEnvironmentSingleton getInstance() {
        return ourInstance;
    }

    private StatementListETNode rootStatementList;

    private CraterExecutionEnvironmentSingleton() {
        this.rootStatementList = new StatementListETNode();
    }

    public StatementListETNode getRootStatementList() {
        return this.rootStatementList;
    }

    public void executeProgram() {
        CraterVariableScope rootVariableScope = new CraterVariableScope();
        CraterExecutionEnvironmentSingleton.loadBuiltinFunctions(rootVariableScope);
        this.rootStatementList.setVariableScope(rootVariableScope);
        this.rootStatementList.execute();
    }

    public static CraterVariableScope getRootVariableScope() {
        return getInstance().rootStatementList.getVariableScope();
    }

    private static void loadBuiltinFunctions(CraterVariableScope rootScope) {
        System.out.println("loading builtin functions...");
        rootScope.nonRecursiveSetValue("len", new LengthBuiltinFunction());
        rootScope.nonRecursiveSetValue("map", new MapBuiltinFunction());
        rootScope.nonRecursiveSetValue("puts", new PrintBuiltinFunction(false));
        rootScope.nonRecursiveSetValue("put", new PrintBuiltinFunction(true));
        rootScope.nonRecursiveSetValue("gets", new GetStringBuiltinFunction());
        rootScope.nonRecursiveSetValue("list", new ListBuiltinFunction());
    }

    public void addRootStatement(ETNode statement) {
        this.rootStatementList.add(statement);
    }
}
