package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class LoopBreakStatementETNode extends ETNode {

    @Override
    public CDT execute(CraterVariableScope scope) {
        this.parent.handleBreak();
        return CNone.get();
    }
}
