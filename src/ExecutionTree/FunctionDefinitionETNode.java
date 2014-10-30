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

    private CFunction function;

    public FunctionDefinitionETNode(ArrayList<String> parameterNames, ETNode body) {
        this.function = new CFunction(parameterNames, body.setParent(this));
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        this.function.setScope(scope);
        return this.function;
    }
}
