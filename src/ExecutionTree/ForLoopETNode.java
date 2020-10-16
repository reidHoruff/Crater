package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.*;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/15/14.
 */
public class ForLoopETNode extends ETNode {

    private String variableIdent;
    private ETNode feederExpression, body;
    private boolean isExecutionBroken;
    private CraterVariableScope scope, oldParentScope;

    public ForLoopETNode(Token ident, ETNode feederExpression, ETNode body) {
        this.variableIdent = ident.sequence;
        this.feederExpression = feederExpression.setParent(this);
        this.body = body.setParent(this);
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

        this.isExecutionBroken = false;
        CDT expression = this.feederExpression.executeMetaSafe(scope);
        CDT lastValue = CNone.get();

        if (expression instanceof CRange) {
            // this could be simpler but is OPTIMIZED!
            CRange range = expression.toCRange();
            CDT value = range.head.clone();
            MetaCDT wrapped = value.withMetaWrapper();
            scope.nonRecursiveSetValueWithWrapper(this.variableIdent, wrapped);
            while (!this.isExecutionBroken && range.siContains(value).toBool()) {
                wrapped.setData(value);
                lastValue = this.body.executeMetaSafe(scope);
                value = value.siPlus(range.increment);
            }
        } else if (expression instanceof CDict) {
            CDict dict = expression.toCDict();
            for (CDT key : dict.dictionary.keySet()) {
                if (this.isExecutionBroken) break;
                scope.nonRecursiveSetValue(this.variableIdent, key);
                lastValue = this.body.executeMetaSafe(scope);
            }
        } else if (expression instanceof CList) {
            CList list = expression.toCList();
            for (CDT value : list.getItems()) {
                if (this.isExecutionBroken) break;
                scope.nonRecursiveSetValue(this.variableIdent, value);
                lastValue = this.body.executeMetaSafe(scope);
            }
        } else {
            throw new CraterExecutionException("invalid expression type for for loop");
        }

        return lastValue;
    }

    @Override
    protected void handleBreak() {
        this.isExecutionBroken = true;
        // break stops here...
    }

    @Override
    protected void handleReturn() {
        this.isExecutionBroken = true;
        super.handleReturn();
    }
}
