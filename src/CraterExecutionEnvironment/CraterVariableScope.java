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

    public CraterVariableScope extend() {
        return new CraterVariableScope(this);
    }

    private boolean hasVariable(String identifier) {
        return this.variables.containsKey(identifier);
    }

    public boolean recursiveHasVariable(String name) {
        if (this.hasVariable(name)) {
            return true;
        }

        return this.parentScope != null && this.parentScope.recursiveHasVariable(name);
    }

    private MetaCDT createVariable(String identifier) {
        MetaCDT wrapper = new MetaCDT();
        this.variables.put(identifier, wrapper);
        return wrapper;
    }

    public void nonRecursiveSetValue(String name, CDT value) {
        this.variables.put(name, value.withMetaWrapper());
    }

    public void nonRecursiveSetValueWithWrapper(String name, MetaCDT value) {
        this.variables.put(name, value);
    }

    private MetaCDT recursivelyFind(String identifier) {
        if (this.hasVariable(identifier)) {
            return this.variables.get(identifier);
        }

        if (this.parentScope != null) {
            return this.parentScope.recursivelyFind(identifier);
        }

        return null;
    }

    public MetaCDT getVariableReference(String identifier) {

        MetaCDT result = this.recursivelyFind(identifier);

        if (result != null) {
            return result;
        } else {
            // we don't have it but here's a free pass to create it
            return new VariableAssignMetaCDT(this, identifier);
        }
    }
}
