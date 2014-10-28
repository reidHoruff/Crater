package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class NOPETNode extends ETNode {

    @Override
    public CDT execute(CraterVariableScope scope) {
        //Fuck you I won't do whatcha tell me...
        return CNone.get();
    }
}
