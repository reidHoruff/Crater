package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import Exceptions.CraterInternalException;
import NativeDataTypes.CBoolean;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;

/**
 * Created by reidhoruff on 10/10/14.
 */
public class WhileConditionETNode extends ETNode {

    private ETNode condition, body;

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
        int runs = 0;
        while (getCondition()) {
            runs += 1;
            this.body.execute();
        }
        return new CInteger(runs);
    }
}
