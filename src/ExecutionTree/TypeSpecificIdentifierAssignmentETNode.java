package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import CraterTyping.TypeEnforcer;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;
import NativeDataTypes.TypeProtectionMetaCDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/21/14.
 */
public class TypeSpecificIdentifierAssignmentETNode extends ETNode {

    private ETNode rightSide;
    private Token identifierToken;
    private TypeEnforcer typeEnforcer;

    public TypeSpecificIdentifierAssignmentETNode(Token typeKeyWord, Token identifierToken, ETNode rightSide) {
        this.typeEnforcer = new TypeEnforcer(typeKeyWord);
        this.identifierToken = identifierToken;
        this.rightSide = rightSide.setParent(this);
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        CDT right = rightSide.executeMetaSafe(scope);
        scope.nonRecursiveSetValueWithWrapper(
                this.identifierToken.sequence,
                new TypeProtectionMetaCDT(this.typeEnforcer, right)
        );
        return CNone.get();
    }
}
