package BuiltinFunctions;

import CraterExecutionEnvironment.CraterVariableScope;
import ExecutionTree.FunctionDefinitionETNode;
import NativeDataTypes.CDT;
import NativeDataTypes.CFunction;

/**
 * Created by reidhoruff on 10/27/14.
 */
public abstract class CBuiltinFunction extends CFunction {
    public CBuiltinFunction() {
        super();
    }

    @Override
    public void setScope(CraterVariableScope scope) {
        //pass...
    }

    @Override
    public CFunction cloneWithScope(CraterVariableScope scope) {
        return this;
    }
}
