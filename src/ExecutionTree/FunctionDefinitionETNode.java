package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import NativeDataTypes.CFunction;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class FunctionDefinitionETNode extends ETNode {
    private ArrayList<String> parameterNames;
    private ETNode body;
    private CraterVariableScope scope;

    public FunctionDefinitionETNode(ArrayList<String> parameterNames, ETNode body) {
        this.body = body.setParent(this);
        this.parameterNames = parameterNames;
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        this.scope = scope;
        // this will be called each time the function is defined
        // the body should not execute, however, whe parameters should
        // since they can have default value expressions that are dependent
        // on the parent variable scope

        return new CFunction(this);
    }

    /**
     * this is different from ETNode execute functions.
     * this is called then the actual function is being called upon
     * from the CFunction CDT...
     */
    public CDT executeWith(ArrayList<CDT> argumentValues) {

        CraterVariableScope instanceScope = scope.extend();

        if (argumentValues.size() > this.parameterNames.size()) {
            throw new CraterExecutionException("invalid number of arguments");
        }

        for (int i = 0; i < argumentValues.size(); i++) {
            instanceScope.nonRecursiveSetValue(this.parameterNames.get(i), argumentValues.get(i));
        }

        return this.body.executeMetaSafe(instanceScope);
    }

    public CDT executeWith(CDT singleArgument) {
        ArrayList<CDT> values = new ArrayList<CDT>();
        values.add(singleArgument);
        return this.executeWith(values);
    }
}
