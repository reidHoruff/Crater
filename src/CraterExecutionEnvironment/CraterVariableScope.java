package CraterExecutionEnvironment;

import Exceptions.CraterInternalException;
import NativeDataTypes.*;

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
        this.variables.put(name, value.withMetaWrapper());
    }

    public void nonRecursiveSetFinalValue(String name, CDT value) {
        this.variables.put(name, new FinalMetaCDT(value.metaSafe()));
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
