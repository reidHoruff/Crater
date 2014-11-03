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
    private ArrayList<ETNode> functions, variables, staticFunctions, staticPrivateFunctions;
    private ETNode constructorDefinition;

    public CClass(String className) {
        this.className = className;

        this.functions = new ArrayList<ETNode>();
        this.variables = new ArrayList<ETNode>();

        this.staticFunctions = new ArrayList<ETNode>();
        this.staticPrivateFunctions = new ArrayList<ETNode>();
    }

    public void addFunction(ETNode child) {
        this.functions.add(child);
    }

    public void addStaticFunction(ETNode child) {
        this.staticFunctions.add(child);
    }

    public void addStaticPrivateFunction(ETNode child) {
        this.staticPrivateFunctions.add(child);
    }

    public void addVariable(ETNode child) {
        this.variables.add(child);
    }

    public void setConstructorDefinition(ETNode constructor) {
        this.constructorDefinition = constructor;
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
        if (this.constructorDefinition != null) {
            this.constructorDefinition.executeMetaSafe(scope);
        }

        for (ETNode variableDefinition: this.variables) {
            variableDefinition.executeMetaSafe(scope);
        }

        for (ETNode functionDefinition: this.functions) {
            functionDefinition.executeMetaSafe(scope);
        }

        for (ETNode function: this.staticFunctions) {
            function.executeMetaSafe(scope);
        }

        for (ETNode function: this.staticPrivateFunctions) {
            function.executeMetaSafe(scope);
        }

        return this;
    }

    @Override
    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        /*
        if (accessor.isOrIsDescendentOf(this.staticScope)) {
            System.out.println("PUBLIC ACCESS");
        } else {
            System.out.println("PRIVATE ACCESS");
        }
        */

        if (this.staticScope.recursiveHasVariable(identifier)) {
            return this.staticScope.getVariableReference(identifier);
        }

        return super.siAccessMember(identifier, accessor);
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
