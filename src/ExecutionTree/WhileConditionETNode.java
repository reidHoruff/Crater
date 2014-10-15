package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import Exceptions.CraterInternalException;
import NativeDataTypes.CBoolean;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/10/14.
 */
public class WhileConditionETNode extends ETNode {

    private ETNode condition, body;
    protected boolean isLoopBroken = false;

    public WhileConditionETNode(ETNode condition, ETNode body) {
        this.condition = condition.setParent(this);
        this.body = body.setParent(this);
    }

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.condition.setVariableScope(getVariableScope());
        this.body.setVariableScope(getVariableScope());
    }

    public boolean getCondition() {
        CDT condition = this.condition.executeMetaSafe();

        if (condition instanceof CBoolean) {
            return condition.toBool();
        }

        throw new CraterExecutionException("while conditional must be boolean");
    }

    @Override
    public CDT execute() {
        this.isLoopBroken = false;
        CDT lastValue = new CNone();
        while (!this.isLoopBroken && getCondition()) {
            lastValue = this.body.execute();
        }
        return lastValue;
    }

    @Override
    protected void handleBreak() {
        this.isLoopBroken = true;
    }
}
