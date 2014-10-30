package NativeDataTypes;

import CraterExecutionEnvironment.CraterVariableScope;
import ExecutionTree.ETNode;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/27/14.
 */
public class CClass extends CDT {
    private CraterVariableScope staticScope;
    private String className;
    private ArrayList<ETNode> functions, variables;
    private ETNode constructor;

    public CClass(String className) {
        this.className = className;

        this.functions = new ArrayList<ETNode>();
        this.variables = new ArrayList<ETNode>();
    }

    public void addFunction(ETNode child) {
        this.functions.add(child);
    }

    public void addVariable(ETNode child) {
        this.variables.add(child);
    }

    public void setConstructor(ETNode constructor) {
        this.constructor = constructor;
    }

    public void setStaticScope(CraterVariableScope scope) {
        this.staticScope = scope;
        MetaCDT self = this.withMetaWrapper().setFinal(true);
        this.staticScope.nonRecursiveSetValue("static", self);
        this.staticScope.nonRecursiveSetValue("this", self);
    }

    /**
     * runs through all of the children ETNodes
     * end executes them in the context
     * of this variable scope
     *
     * basically a warpper for ETNode's executeXXX()
     */
    public CClass execute() {
        return this.executeOnScope(this.staticScope);
    }

    public CClass executeOnScope(CraterVariableScope scope) {
        if (this.constructor != null) {
            this.constructor.executeMetaSafe(scope);
        }

        for (ETNode variableDefinition: this.variables) {
            variableDefinition.executeMetaSafe(scope);
        }

        for (ETNode functionDefinition: this.functions) {
            functionDefinition.executeMetaSafe(scope);
        }

        return this;
    }

    @Override
    public CDT siAccessMember(String identifier) {
        if (this.staticScope.recursiveHasVariable(identifier)) {
            return this.staticScope.getVariableReference(identifier);
        }

        return super.siAccessMember(identifier);
    }

    public String getClassName() {
        return this.className;
    }

    public CraterVariableScope getStaticScope() {
        return this.staticScope;
    }

    @Override
    public CDT siInstantiate(ArrayList<CDT> arguments) {
        return new CInstance(this);
    }

    @Override
    public String getTypeName() {
        return "class " + this.className;
    }

    @Override
    public String toString() {
        return "class " + this.className;
    }

}
