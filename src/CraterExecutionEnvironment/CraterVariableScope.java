package CraterExecutionEnvironment;

import BuiltinFunctions.*;
import CraterHelpers.clog;
import Exceptions.CraterExecutionException;
import ExecutionTree.ETNode;
import NativeDataTypes.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class CraterVariableScope {

    private HashMap<String, MetaCDT> variables;
    private HashMap<Integer, MetaCDT> hashVariables;
    private HashMap<Integer, String> hashToName;

    private CraterVariableScope parentScope;

    private static long scopeIdCounter = 0;
    private long scopeId;

    public CraterVariableScope() {
        this(null);
    }

    private static CDT checkForBuiltin(String name) {
        if (name.equals("len")) {
            return new LengthBuiltinFunction();
        }

        if (name.equals("map")) {
            return new MapBuiltinFunction();
        }

        if (name.equals("puts")) {
            return new PrintBuiltinFunction(false);
        }

        if (name.equals("put")) {
            return new PrintBuiltinFunction(true);
        }

        if (name.equals("gets")) {
            return new GetStringBuiltinFunction();
        }

        if (name.equals("list")) {
            return new ListBuiltinFunction();
        }

        return null;
    }

    public long getScopeId() {
        return this.scopeId;
    }

    public CraterVariableScope(CraterVariableScope parent) {
        this.variables = new HashMap<String, MetaCDT>();
        this.hashVariables = new HashMap<Integer, MetaCDT>();
        this.hashToName = new HashMap<Integer, String>();
        this.parentScope = parent;
        this.scopeId = scopeIdCounter;
        scopeIdCounter += 1;
        this.nonRecursiveSetValue("lvars", new ScopeAwareFunction(this) {
            @Override
            public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                return this.scope.localToDict();
            }
        });
        this.nonRecursiveSetValue("pvars", new ScopeAwareFunction(this) {
            @Override
            public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                return this.scope.getParentScope().localToDict();
            }
        });
        this.nonRecursiveSetValue("avars", new ScopeAwareFunction(this) {
            @Override
            public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                return this.scope.getParentScope().allToDict();
            }
        });
    }

    public CraterVariableScope getParentScope() {
        return this.parentScope;
    }

    public CraterVariableScope extend() {
        return new CraterVariableScope(this);
    }

    public boolean immediatelyContainsVariable(String identifier) {
        return this.variables.containsKey(identifier);
    }

    public boolean immediatelyContainsVariable(int identifier) {
        return this.hashVariables.containsKey(identifier);
    }

    public boolean recursiveHasVariable(String name) {
        if (this.immediatelyContainsVariable(name)) {
            return true;
        }

        return this.parentScope != null && this.parentScope.recursiveHasVariable(name);
    }

    public CDict localToDict() {
       CDict dict = new CDict();

        for (String key: this.variables.keySet()) {
            dict.directPut(new CString(key), this.variables.get(key));
        }

        return dict;
    }

    public CDict allToDict() {
        CDict dict = new CDict();
        this.recursiveBuildCDict(dict);
        return dict;
    }

    private void recursiveBuildCDict(CDict dict) {
        for (String key: this.variables.keySet()) {
            dict.directPut(new CString(key), this.variables.get(key));
        }

        if (this.parentScope != null) {
            this.parentScope.recursiveBuildCDict(dict);
        }
    }

    private MetaCDT createVariable(String identifier) {
        MetaCDT wrapper = new MetaCDT();
        assert false == this.variables.containsKey(identifier);
        this.variables.put(identifier, wrapper);
        this.hashVariables.put(identifier.hashCode(), wrapper);
        this.hashToName.put(identifier.hashCode(), identifier);
        return wrapper;
    }

    public void nonRecursiveSetValue(String name, CDT value) {
        this.nonRecursiveSetValueWithWrapper(name, value.withMetaWrapper());
    }

    public void nonRecursiveSetValueWithWrapper(String name, MetaCDT value) {
        this.variables.put(name, value);
        this.hashVariables.put(name.hashCode(), value);
        this.hashToName.put(name.hashCode(), name);
    }

    private MetaCDT recursivelyFind(String identifier) {
        if (this.immediatelyContainsVariable(identifier)) {
            return this.variables.get(identifier);
        }

        if (this.parentScope != null) {
            return this.parentScope.recursivelyFind(identifier);
        }

        return null;
    }

    private MetaCDT recursivelyFind(int identifier) {
        if (this.immediatelyContainsVariable(identifier)) {
            return this.hashVariables.get(identifier);
        }

        if (this.parentScope != null) {
            return this.parentScope.recursivelyFind(identifier);
        }

        return null;
    }

    public boolean isOrIsDescendentOf(CraterVariableScope scope) {
        CraterVariableScope curr = this;

        while (curr != null) {
            if (scope == curr) {
                return true;
            }
            curr = curr.parentScope;
        }

        return false;
    }

    public void clear() {
       this.variables.clear();
    }

    public MetaCDT getVariableReference(String identifier) {

        MetaCDT result = this.recursivelyFind(identifier);

        if (result == null) {
            throw new CraterExecutionException("variable [" + identifier + "] must first be defined with the var keyword");
        }

        return result;
    }

    public MetaCDT getVariableReference(int identifier, String nameForError) {

        MetaCDT result = this.recursivelyFind(identifier);

        if (result == null) {
            throw new CraterExecutionException("variable [" + nameForError + "] must first be defined with the var keyword");
        }

        return result;
    }
}
