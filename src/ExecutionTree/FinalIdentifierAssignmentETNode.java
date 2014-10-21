package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class FinalIdentifierAssignmentETNode extends ETNode {

    private ETNode rightSide;
    private Token identifierToken;

    public FinalIdentifierAssignmentETNode(Token identifierToken, ETNode rightSide) {
        this.identifierToken = identifierToken;
        this.rightSide = rightSide.setParent(this);
    }

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.rightSide.setVariableScope(scope);
    }

    @Override
    public CDT execute() {
        CDT right = rightSide.executeMetaSafe();
        this.getVariableScope().nonRecursiveSetFinalValue(this.identifierToken.sequence, right);
        return CNone.get();
    }
}
