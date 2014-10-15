package NativeDataTypes;

import CraterExecutionEnvironment.CraterVariableScope;

/**
 * Created by reidhoruff on 10/14/14.
 */
public class VariableAssignMetaCDT extends MetaCDT {

    private CraterVariableScope scope;
    private String variableName;

    public VariableAssignMetaCDT(CraterVariableScope scope, String name) {
        this.scope = scope;
        this.variableName = name;
    }

    @Override
    public void setData(CDT data) {
        this.scope.nonRecursiveSetValue(this.variableName, data);
        this.data = data;
    }
}
