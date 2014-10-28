package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CBoolean;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

/**
 * Created by reidhoruff on 10/10/14.
 */
public class IfConditionETNode extends ETNode {

    private ETNode condition, body, elseBody;

    public IfConditionETNode(ETNode condition, ETNode body, ETNode elseBody) {
        this.condition = condition.setParent(this);
        this.body = body.setParent(this);

        if (elseBody != null) {
            this.elseBody = elseBody.setParent(this);
        }
    }

    public boolean getCondition(CraterVariableScope scope) {
        CDT condition = this.condition.executeMetaSafe(scope);

        if (condition instanceof CBoolean) {
            return condition.toBool();
        }

        throw new CraterExecutionException("if conditional must be boolean");
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        scope = scope.extend();
        CDT lastValue = CNone.get();

        if (getCondition(scope)) {
            lastValue = this.body.executeMetaSafe(scope);
        } else if (this.elseBody != null) {
            lastValue = this.elseBody.executeMetaSafe(scope);
        }

        return lastValue;
    }
}
