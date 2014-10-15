package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterParserException;
import NativeDataTypes.CDT;
import NativeDataTypes.MetaCDT;

/**
 * Created by reidhoruff on 10/14/14.
 */
public class IdentifierAssignmentETNode extends ETNode {

    private ETNode reference, expression;

    public IdentifierAssignmentETNode(ETNode reference, ETNode expression) {
        this.reference = reference.setParent(this);
        this.expression = expression.setParent(this);
    }

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.reference.setVariableScope(scope);
        this.expression.setVariableScope(scope);
    }

    @Override
    public CDT execute() {
        MetaCDT left = this.reference.executeAndExpectMetaCDT();
        CDT right = this.expression.executeMetaSafe();
        left.setData(right);
        return left.metaSafe();
    }
}
