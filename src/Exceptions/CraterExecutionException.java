package Exceptions;

import CraterExecutionEnvironment.CExecSingleton;

import java.util.Stack;

/**
 * Created by reidhoruff on 10/8/14.
 */

public class CraterExecutionException extends RuntimeException {

    public CraterExecutionException(String msg) {
        System.out.println();
        System.out.println(msg);
        System.out.println();
        System.out.println(CExecSingleton.get().getStatementStack().peek().toString());

        Stack callStack = CExecSingleton.get().getCallStack();

        while (!callStack.empty()) {
            System.out.println("\n@ " + callStack.pop().toString());
        }

        System.exit(1);
    }
}
