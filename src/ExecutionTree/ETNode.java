package ExecutionTree;

import CraterExecutionEnvironment.CExecSingleton;
import CraterExecutionEnvironment.CraterVariableScope;
import CraterExecutionEnvironment.ExecutionStackFrame;
import CraterTyping.TypeEnforcer;
import Exceptions.CraterInternalException;
import NativeDataTypes.CDT;
import NativeDataTypes.MetaCDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/8/14.
 */

public abstract class ETNode {
    public ETNode parent;
    private TypeEnforcer typeEnforcer;
    public Token spawningToken;

    public ETNode setParent(ETNode parent) {
        this.parent = parent;
        return this;
    }

    public ETNode setSpawningToken(Token token) {
        // only set once, subsequent sets would
        // be tokens of parent expressions, therefore would be less specific
        if (this.spawningToken == null) {
            //System.out.println("\nSETTING SPAWNED\n" + token.toString() + " [" + this.getClass().toString());
            this.spawningToken = token;
        }

        // for chaining
        return this;
    }

    public abstract CDT execute(CraterVariableScope scope);

    protected final MetaCDT executeAndExpectMetaCDT(CraterVariableScope scope) {
        this.pushFrame();
        CDT data = this.execute(scope);
        if (!(data instanceof MetaCDT)) {
            throw new CraterInternalException("expected MetaCDT");
        }
        this.popFrame();
        return (MetaCDT)data;
    }

    protected final CDT executeMetaSafe(CraterVariableScope scope) {
        this.pushFrame();
        CDT value = this.execute(scope).metaSafe();
        if (this.hasTypeEnforcement()) {
            if (this.typeEnforcer.isCorrectType(value)) {
                this.popFrame();
                return value;
            } else {
                throw typeEnforcer.getException(value);
            }
        } else {
            this.popFrame();
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

    /**
     * execution stack frames
     */

    protected void pushFrame() {
        if (this.spawningToken != null) {
            CExecSingleton.get().getStatementStack().push(new ExecutionStackFrame(this.spawningToken));
        }
    }

    protected void popFrame() {
        if (this.spawningToken != null) {
            ExecutionStackFrame popped = CExecSingleton.get().getStatementStack().pop();
        }
    }
}
