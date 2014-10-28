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

    public ForLoopETNode(Token ident, ETNode feederExpression, ETNode body) {
        this.variableIdent = ident.sequence;
        this.feederExpression = feederExpression.setParent(this);
        this.body = body.setParent(this);
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        scope = scope.extend();
        this.isExecutionBroken = false;
        CDT expression = this.feederExpression.executeMetaSafe(scope);
        CDT lastValue = CNone.get();

        if (expression instanceof CRange) {
            CRange range = expression.toCRange();
            for (int i = range.head; i < range.tail; i += range.increment) {
                if (this.isExecutionBroken) break;
                scope.nonRecursiveSetValue(this.variableIdent, new CInteger(i));
                lastValue = this.body.executeMetaSafe(scope);
            }
        } else if (expression instanceof CInteger) {
            int top = expression.toInt();
            for (int i = 0; i < top; i++) {
                if (this.isExecutionBroken) break;
                scope.nonRecursiveSetValue(this.variableIdent, new CInteger(i));
                lastValue = this.body.executeMetaSafe(scope);
            }
        } else if (expression instanceof CDict) {
            CDict dict = expression.toCDict();
            for (CDT key : dict.dictionary.keySet()) {
                if (this.isExecutionBroken) break;
                scope.nonRecursiveSetValue(this.variableIdent, key);
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
