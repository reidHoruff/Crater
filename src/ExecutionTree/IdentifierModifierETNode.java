package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import Exceptions.CraterParserException;
import NativeDataTypes.*;
import Scanning.Token;


/**
 * Created by reidhoruff on 10/8/14.
 */

/**
 * +=, -= *= etc...
 */
public class IdentifierModifierETNode extends ETNode {

    private Token modifierToken;
    private ETNode leftSide, rightSide;

    public IdentifierModifierETNode(ETNode leftSide, Token modifierToken, ETNode rightSide) {
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

        switch (this.modifierToken.token) {
            case C_EQUALS:
                left.setData(right);
                return left.metaSafe();
            case D_PLUS_EQUALS:
                return this.plusEquals(left.metaSafe(), right);
        }

        throw new CraterParserException("Invalid operation");
    }

    public static CDT plusEquals(CDT left, CDT right) {
        if (left instanceof CList) {
            ((CList) left).addCDT(right.clone());
            return left;
        }

        if (left instanceof CInteger && right instanceof CInteger) {
            ((CInteger) left).intValue += ((CInteger) right).intValue;
            return left;
        }

        throw new CraterExecutionException("+= operator must be of for [list += *] or [int += int]");
    }
}
