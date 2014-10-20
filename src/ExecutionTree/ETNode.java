package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import CraterTyping.TypeEnforcer;
import Exceptions.CraterInternalException;
import NativeDataTypes.CDT;
import NativeDataTypes.MetaCDT;

/**
 * Created by reidhoruff on 10/8/14.
 */

public abstract class ETNode {
    public ETNode parent;
    public CraterVariableScope variableScope;
    private TypeEnforcer typeEnforcer;

    public ETNode setParent(ETNode parent) {
        this.parent = parent;
        return this;
    }

    public void setVariableScope(CraterVariableScope scope) {
        this.variableScope = scope;
        this.setChildrenVariableScope(scope);
    }

    public abstract void setChildrenVariableScope(CraterVariableScope scope);

    public CraterVariableScope getVariableScope() {
        return this.variableScope;
    }

    public abstract CDT execute();

    public void add(ETNode child) {
    }

    protected final MetaCDT executeAndExpectMetaCDT() {
        CDT data = this.execute();
        if (!(data instanceof MetaCDT)) {
            throw new CraterInternalException("expected MetaCDT");
        }

        return (MetaCDT)data;
    }

    protected final CDT executeMetaSafe() {
        CDT value = this.execute().metaSafe();
        if (this.hasTypeEnforcement()) {
            if (this.typeEnforcer.isCorrectType(value)) {
                return value;
            } else {
                throw typeEnforcer.getException(value);
            }
        } else {
            return value;
        }
    }

    protected boolean hasTypeEnforcement() {
        return this.typeEnforcer != null;
    }

    public void setTypeEnforcer(TypeEnforcer enforcer) {
        this.typeEnforcer = enforcer;
    }

    public void putSpaces(int spaces) {
        while (spaces --> 0) {
            System.out.print(" ");
        }
    }

    public void print(int level) {
        putSpaces(level);
        System.out.println("???");
    }

    protected void handleBreak() {
        if (this.parent != null) {
            this.parent.handleBreak();
        }
    }

    protected void handleReturn() {
        if (this.parent != null) {
            this.parent.handleReturn();
        }
    }
}
