package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CString;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class StringLiteralETNode extends ETNode {
    private CString value = null;

    public StringLiteralETNode(Token valueToken) {
        this.value = new CString(valueToken);
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        return this.value;
    }
}
