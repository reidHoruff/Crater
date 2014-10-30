package BuiltinFunctions;

import CraterExecutionEnvironment.CraterVariableScope;

/**
 * Created by reidhoruff on 10/28/14.
 */
public class ScopeAwareFunction extends CBuiltinFunction {

    protected CraterVariableScope scope;

    public ScopeAwareFunction(CraterVariableScope scope) {
        this.scope = scope;
    }
}
