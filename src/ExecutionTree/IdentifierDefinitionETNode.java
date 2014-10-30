package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class IdentifierDefinitionETNode extends ETNode {

    private ETNode rightSide;
    private Token identifierToken;
    private boolean isFinal;

    public IdentifierDefinitionETNode(Token identifierToken, ETNode rightSide, boolean isFinal) {
        this.identifierToken = identifierToken;
        this.rightSide = rightSide.setParent(this);
        this.isFinal = isFinal;
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        CDT right = rightSide.executeMetaSafe(scope);

        if (scope.immediatelyContainsVariable(this.identifierToken.sequence)) {
            throw new CraterExecutionException("cannot redefine [" + this.identifierToken.sequence + "] within this scope");
        }

        scope.nonRecursiveSetValueWithWrapper(
                this.identifierToken.sequence,
                right.withMetaWrapper().setFinal(this.isFinal)
        );

        return right;
    }
}
