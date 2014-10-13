package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CFunction;
import NativeDataTypes.MetaCDT;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class FunctionCallETNode extends ETNode {
    private ETNode functionReference;
    private ArrayList<ETNode> parameters;

    public FunctionCallETNode(ETNode functionReference, ArrayList<ETNode> parameters) {
        this.functionReference = functionReference.setParent(this);
        this.parameters = new ArrayList<ETNode>();
        for (ETNode parameter : parameters) {
            this.parameters.add(parameter.setParent(this));
        }
    }

    /**
    @Override
    public void setVariableScope(CraterVariableScope scope) {
        this.variableScope = new CraterVariableScope(scope);
        this.setChildrenVariableScope(this.variableScope);
    }
     **/

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.functionReference.setVariableScope(scope);
        for (ETNode parameter : this.parameters) {
            parameter.setVariableScope(scope);
        }
    }

    @Override
    public CDT execute() {
        CDT functionRef = this.functionReference.executeMetaSafe();

        ArrayList<CDT> argumentValues = new ArrayList<CDT>(5);
        for (ETNode parameterExpression : this.parameters) {
            argumentValues.add(parameterExpression.executeMetaSafe());
        }

        return functionRef.callWithArguments(argumentValues);
    }
}
