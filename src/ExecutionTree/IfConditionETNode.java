package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CBoolean;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
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

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.condition.setVariableScope(getVariableScope());
        this.body.setVariableScope(getVariableScope());
        if (this.elseBody != null) {
            this.elseBody.setVariableScope(getVariableScope());
        }
    }

    public boolean getCondition() {
        CDT condition = this.condition.executeMetaSafe();

        if (condition instanceof CBoolean) {
            return condition.toBool();
        }

        throw new CraterExecutionException("if conditional must be boolean");
    }

    @Override
    public CDT execute() {
        CDT lastValue = new CNone();

        if (getCondition()) {
            lastValue = this.body.execute();
        } else if (this.elseBody != null) {
            lastValue = this.elseBody.execute();
        }

        return lastValue;
    }

    public void print(int level) {
    }
}
