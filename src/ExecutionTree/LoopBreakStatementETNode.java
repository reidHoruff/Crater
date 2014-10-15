package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class LoopBreakStatementETNode extends ETNode {

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
    }

    @Override
    public CDT execute() {
        this.parent.handleBreak();
        return new CNone();
    }
}
