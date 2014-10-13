package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import NativeDataTypes.CFunction;
import NativeDataTypes.CNone;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class FunctionDefinitionETNode extends ETNode {
    private ArrayList<String> parameterNames;
    private ETNode body;

    public FunctionDefinitionETNode(ArrayList<String> parameterNames, ETNode body) {
        this.body = body.setParent(this);
        this.parameterNames = parameterNames;
    }

    @Override
    public void setVariableScope(CraterVariableScope scope) {
        this.variableScope = new CraterVariableScope(scope);
        this.setChildrenVariableScope(this.variableScope);
    }

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.body.setVariableScope(scope);
    }

    @Override
    public CDT execute() {
        // this will be called each time the function is defined
        // the body should not execute, however, whe parameters should
        // since they can have default value expressions that are dependent
        // on the parent variable scope

        for (String parameterName : this.parameterNames) {
            break;
        }

        return new CFunction(this);
    }

    /**
     * this is different from ETNode execute functions.
     * this is called then the actual function is being called upon
     * from the CFunction CDT...
     */
    public CDT executeWith(ArrayList<CDT> argumentValues) {
        if (argumentValues.size() != this.parameterNames.size()) {
            throw new CraterExecutionException("invalid number of arguments");
        }

        for (int i = 0; i < argumentValues.size(); i++) {
            this.getVariableScope().nonRecursiveSetValue(this.parameterNames.get(i), argumentValues.get(i));
        }

        return body.executeMetaSafe();
    }

    public CDT executeWith(CDT singleArgument) {
        this.getVariableScope().nonRecursiveSetValue(this.parameterNames.get(0), singleArgument);
        return body.executeMetaSafe();
    }
}
