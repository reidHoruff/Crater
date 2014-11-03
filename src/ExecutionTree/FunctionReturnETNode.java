package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class FunctionReturnETNode extends ETNode {
    private ETNode finalStatement;

    public FunctionReturnETNode(ETNode finalStatement) {
        this.finalStatement = finalStatement.setParent(this);
    }

    public FunctionReturnETNode() {
        this.finalStatement = null;
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        if (this.finalStatement != null) {
            CDT returnValue = this.finalStatement.executeMetaSafe(scope);
            this.handleReturn();
            return returnValue;
        } else {
            this.handleReturn();
            return CNone.get();
        }
    }
}
