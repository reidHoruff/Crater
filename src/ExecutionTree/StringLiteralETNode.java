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

    public StringLiteralETNode(String value) {
        this.value = new CString(value);
    }

    public StringLiteralETNode(Token valueToken) {
        this.value = new CString(valueToken.sequence);
    }

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        ;
    }

    @Override
    public CDT execute() {
        return this.value;
    }
}
