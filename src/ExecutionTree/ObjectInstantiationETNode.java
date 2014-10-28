package ExecutionTree;

import CraterExecutionEnvironment.CExecSingleton;
import CraterExecutionEnvironment.CraterVariableScope;
import CraterExecutionEnvironment.FunctionCallStackFrame;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/27/14.
 */
public class ObjectInstantiationETNode extends ETNode {

    private ETNode classIdentifier;
    private ArrayList<ETNode> parameters;

    public ObjectInstantiationETNode(ETNode classIdentifier, ArrayList<ETNode> parameters) {
        this.classIdentifier = classIdentifier.setParent(this);
        this.parameters = new ArrayList<ETNode>();
        for (ETNode parameter : parameters) {
            this.parameters.add(parameter.setParent(this));
        }
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        return CNone.get();
    }
}
