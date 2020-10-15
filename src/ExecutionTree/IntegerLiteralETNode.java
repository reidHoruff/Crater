package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class IntegerLiteralETNode extends ETNode {

    public long value;

    public IntegerLiteralETNode(Token token) {
        this.value = Long.parseLong(token.sequence);
    }

    public IntegerLiteralETNode(int value) {
        this.value = value;
    }

    public CDT execute(CraterVariableScope scope) {
        return new CInteger(this.value);
    }
}
