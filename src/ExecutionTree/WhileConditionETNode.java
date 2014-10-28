package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CBoolean;
import NativeDataTypes.CDT;
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

    public boolean getCondition(CraterVariableScope scope) {
        CDT condition = this.condition.executeMetaSafe(scope);

        if (condition instanceof CBoolean) {
            return condition.toBool();
        }

        throw new CraterExecutionException("while conditional must be boolean");
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        scope = scope.extend();
        this.isLoopBroken = false;
        CDT lastValue = CNone.get();
        while (!this.isLoopBroken && getCondition(scope)) {
            lastValue = this.body.executeMetaSafe(scope);
        }
        return lastValue;
    }

    @Override
    protected void handleBreak() {
        this.isLoopBroken = true;
    }
}
