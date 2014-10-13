package NativeDataTypes;

import ExecutionTree.FunctionDefinitionETNode;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class CFunction extends CDT {
    public FunctionDefinitionETNode function;

    public CFunction(FunctionDefinitionETNode function) {
        this.function = function;
    }

    @Override
    public String getTypeName() {
        return "function";
    }

    @Override
    public String toString() {
        return "function";
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values) {
        return this.function.executeWith(values);
    }

    @Override
    public CDT callWithSingleArgument(CDT value) {
        return this.function.executeWith(value);
    }
}
