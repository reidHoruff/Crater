package CraterExecutionEnvironment;

import Scanning.Token;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class ExecutionStackFrame {

    private Token spawningToken;

    public ExecutionStackFrame(Token token) {
        this.spawningToken = token;
    }

    @Override
    public String toString() {
        return "@ " + this.spawningToken.toString();
    }
}
