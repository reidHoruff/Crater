package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/10/14.
 */
public class NoneLiteralETNode extends ETNode {

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
    }

    @Override
    public CDT execute() {
        return CNone.get();
    }
}
