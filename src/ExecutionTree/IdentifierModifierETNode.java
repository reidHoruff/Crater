package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import Exceptions.CraterParserException;
import NativeDataTypes.*;
import Scanning.Token;
import Scanning.TokenType;


/**
 * Created by reidhoruff on 10/8/14.
 */

/**
 * +=, -= *= etc...
 */
public class IdentifierModifierETNode extends ETNode {

    private TokenType modifierToken;
    private ETNode leftSide, rightSide;

    public IdentifierModifierETNode(ETNode leftSide, TokenType modifierToken, ETNode rightSide) {
        this.leftSide = leftSide.setParent(this);
        this.modifierToken = modifierToken;
        this.rightSide = rightSide.setParent(this);
    }

    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.leftSide.setVariableScope(scope);
        this.rightSide.setVariableScope(scope);
    }

    public CDT execute() {
        MetaCDT left = leftSide.executeAndExpectMetaCDT();
        CDT right = rightSide.executeMetaSafe();

        switch (this.modifierToken) {
            case D_PLUS_EQUALS: left.setData(left.siPlusEquals(right)); break;
            case C_EQUALS: left.setData(right); break;
            default: throw new CraterParserException("Invalid operation");
        }

        return CNone.get();
    }
}
