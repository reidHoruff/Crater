package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;

/**
 * Created by reidhoruff on 10/14/14.
 */
public class CDTLiteralETNode extends ETNode {

    public CDT value;

    public CDTLiteralETNode(CDT value) {
        this.value = value;
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        return this.value;
    }
}
