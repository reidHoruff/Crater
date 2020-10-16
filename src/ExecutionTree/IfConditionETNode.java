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
    private CraterVariableScope scope, oldParentScope;

    public IfConditionETNode(ETNode condition, ETNode body, ETNode elseBody) {
        this.condition = condition.setParent(this);
        this.body = body.setParent(this);

        if (elseBody != null) {
            this.elseBody = elseBody.setParent(this);
        }
    }

    public boolean getCondition(CraterVariableScope scope) {
        CDT condition = this.condition.executeMetaSafe(scope);

        // we only accept bools 'round here. none of that implicit integer!=0, strlen>0 queer shit.
        if (condition instanceof CBoolean) {
            return condition.toBool();
        }

        throw new CraterExecutionException("if conditional must be type boolean, not " + condition.getTypeName());
    }

    @Override
    public CDT execute(CraterVariableScope parentScope) {

        if (scope == null || (parentScope != oldParentScope)) {
            scope = parentScope.extend();
            // we only track parent scope instance in case a parent
            // scope executor swapped out their CraterVariableScope under from
            // us rather than clearing like us good bois.
            oldParentScope = parentScope;
        }
        else {
            scope.clear();
        }

        CDT lastValue = CNone.get();

        if (getCondition(scope)) {
            lastValue = this.body.executeMetaSafe(scope);
        } else if (this.elseBody != null) {
            lastValue = this.elseBody.executeMetaSafe(scope);
        }

        return lastValue;
    }
}
