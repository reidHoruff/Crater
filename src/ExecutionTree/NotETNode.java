package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;

/**
 * Created by reidhoruff on 11/3/14.
 */
public class NotETNode extends ETNode {

    private ETNode not;

    public NotETNode(ETNode not) {
        this.not = not;
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        return this.not.executeMetaSafe(scope).siNot();
    }
}
