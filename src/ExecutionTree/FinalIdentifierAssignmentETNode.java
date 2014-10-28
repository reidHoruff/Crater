package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;
import NativeDataTypes.FinalMetaCDT;
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
    public CDT execute(CraterVariableScope scope) {
        CDT right = rightSide.executeMetaSafe(scope);
        scope.nonRecursiveSetValueWithWrapper(
                this.identifierToken.sequence,
                new FinalMetaCDT(right)
        );
        return CNone.get();
    }
}
