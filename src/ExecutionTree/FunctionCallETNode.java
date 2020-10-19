package ExecutionTree;

import CraterExecutionEnvironment.CExecSingleton;
import CraterExecutionEnvironment.CraterVariableScope;
import CraterExecutionEnvironment.FunctionCallStackFrame;
import NativeDataTypes.CDT;

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
     * all this does is evaluate
     * the parameter values withing the scope
     * from which the function was called
     *
     * these values are then sent over to the CFunction DT
     * where a new scope is created (extending from the scope from which
     * the function was defined.
     *
     * these parameter values are then injected into this newly created
     * scope.
     */
    @Override
    public CDT execute(CraterVariableScope scope) {
        /** mark stack frame */
        FunctionCallStackFrame stackFrame = new FunctionCallStackFrame(this.spawningToken);

        CDT functionRef = this.functionReference.executeMetaSafe(scope);

        ArrayList<CDT> argumentValues = new ArrayList<CDT>(5);

        for (ETNode parameterExpression : this.parameters) {
            CDT parametervalue = parameterExpression.executeMetaSafe(scope);
            stackFrame.addParameterCallingType(parametervalue);
            argumentValues.add(parametervalue);
        }

        CExecSingleton.get().getCallStack().push(stackFrame);

        /**
         * make call
         */
        CDT returnValue = functionRef.callWithArguments(argumentValues, this);

        CExecSingleton.get().getCallStack().pop();

        return returnValue;
    }

    @Override
    protected void handleReturn() {
        return;
    }
}
