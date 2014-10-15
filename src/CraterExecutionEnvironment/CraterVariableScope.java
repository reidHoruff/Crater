package CraterExecutionEnvironment;

import Exceptions.CraterInternalException;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;
import NativeDataTypes.MetaCDT;
import NativeDataTypes.VariableAssignMetaCDT;

import java.util.HashMap;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class CraterVariableScope {

    private HashMap<String, MetaCDT> variables;
    private CraterVariableScope parentScope;

    public CraterVariableScope() {
        this.variables = new HashMap<String, MetaCDT>();
        this.parentScope = null;
    }

    public CraterVariableScope(CraterVariableScope parent) {
        this.variables = new HashMap<String, MetaCDT>();
        this.parentScope = parent;
    }

    private boolean hasVariable(String identifier) {
        return this.variables.containsKey(identifier);
    }

    private MetaCDT createVariable(String identifier) {
        MetaCDT wrapper = new MetaCDT();
        this.variables.put(identifier, wrapper);
        return wrapper;
    }

    public void nonRecursiveSetValue(String name, CDT value) {
        if (value instanceof MetaCDT) {
            throw new CraterInternalException("hey, don't send me a MetaCDT here!");
        }

        this.variables.put(name, value.withMetaWrapper());
    }

    public MetaCDT getVariableReference(String identifier) {
        if (this.hasVariable(identifier)) {
            return this.variables.get(identifier);
        }

        if (this.parentScope != null) {
            MetaCDT parentResult = this.parentScope.getVariableReference(identifier);
            if (parentResult != null) {
                return parentResult;
            }
        }

        // we don't have it but here's a free pass to create it
        return new VariableAssignMetaCDT(this, identifier);
    }
}
