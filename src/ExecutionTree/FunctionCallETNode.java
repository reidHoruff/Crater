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
        CDT returnValue = functionRef.callWithArguments(argumentValues);

        CExecSingleton.get().getCallStack().pop();

        return returnValue;
    }
}
