package NativeDataTypes;

import CraterExecutionEnvironment.CraterVariableScope;
import ExecutionTree.ETNode;
import Scanning.Token;

/**
 * Created by reidhoruff on 11/3/14.
 */
public class FloatLiteralETNode extends ETNode {

    private double value;

    public FloatLiteralETNode(Token token) {
        this.value = Double.parseDouble(token.sequence);
    }

    public CDT execute(CraterVariableScope scope) {
        return new CFloat(this.value);
    }

}
