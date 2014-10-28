package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/10/14.
 */
public class NoneLiteralETNode extends ETNode {

    @Override
    public CDT execute(CraterVariableScope scope) {
        return CNone.get();
    }
}
