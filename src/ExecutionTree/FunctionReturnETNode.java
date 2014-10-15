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
    public void setChildrenVariableScope(CraterVariableScope scope) {
        if (this.finalStatement != null) {
            this.finalStatement.setVariableScope(scope);
        }
    }

    @Override
    public CDT execute() {
        if (this.finalStatement != null) {
            CDT returnValue = this.finalStatement.executeMetaSafe();
            this.handleReturn();
            return returnValue;
        } else {
            return new CNone();
        }
    }
}
