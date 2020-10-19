package NativeDataTypes;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import Exceptions.CraterInternalException;
import ExecutionTree.ETNode;
import ExecutionTree.FunctionDefinitionETNode;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class CFunction extends CDT {

    public ArrayList<String> parameterNames;
    public ETNode body;
    public CraterVariableScope scope;

    public CFunction () {
    }

    public CFunction(ArrayList<String> parameterNames, ETNode body) {
        this.parameterNames = parameterNames;
        this.body = body;
    }

    public void setScope(CraterVariableScope scope) {
        if (this.scope != null) {
            /**
             * use cloneWithScope...
             */
            throw new CraterInternalException("cannot modify scope of existing function");
        }
        this.scope = scope;
    }

    public CFunction cloneWithScope(CraterVariableScope scope) {
        CFunction function = new CFunction(this.parameterNames, this.body);
        function.setScope(scope);
        return function;
    }

    @Override
    public String getTypeName() {
        return "function";
    }

    @Override
    public String toString() {
        return "function";
    }

    /**
     * all you need to do is supply with arguments.
     * this will handle creting a new variable scope
     * (extending from the scope which the function was defined inside of)
     * for the function to execute inside of.
     *
     * eg so recursive calls don't write over
     * variables that are defined within the function.
     */
    @Override
    public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {

        this.body.setParent(parent);

        /**
         * create own variable scope (stack frame like)
         * then call.
         */
        CraterVariableScope callInstanceScope = this.scope.extend();

        return this.cloneWithScope(callInstanceScope).callWithArgumentsInternal(values);
    }

    private CDT callWithArgumentsInternal(ArrayList<CDT> argumentValues) {

        if (argumentValues.size() > this.parameterNames.size()) {
            throw new CraterExecutionException("invalid number of arguments");
        }

        /**
         * values that weren't supplied become None when the function is called
         */

        for (int i = 0; i < this.parameterNames.size(); i++) {
            if (i < argumentValues.size()) {
                scope.nonRecursiveSetValue(this.parameterNames.get(i), argumentValues.get(i));
            } else {
                scope.nonRecursiveSetValue(this.parameterNames.get(i), CNone.get());
            }
        }

        return this.body.executeMetaSafe(scope);
    }

    @Override
    public CDT callWithSingleArgument(CDT value, ETNode parent) {
        /**
         * yea this is pretty shitty...
         */
        ArrayList<CDT> list = new ArrayList<CDT>();
        list.add(value);
        return this.callWithArguments(list, parent);
    }
}
