package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class IntegerLiteralETNode extends ETNode {

    public int value;

    public IntegerLiteralETNode(Token token) {
        this.value = Integer.parseInt(token.sequence);
    }

    public IntegerLiteralETNode(int value) {
        this.value = value;
    }

    public CDT execute() {
        return new CInteger(this.value);
    }

    public void setChildrenVariableScope(CraterVariableScope scope) {
    }
}
