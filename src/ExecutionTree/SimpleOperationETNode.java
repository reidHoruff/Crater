package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/8/14.
 */

/* /*+- etc */
public class SimpleOperationETNode extends ETNode {

    private ETNode left, right;
    private Token operator;

    public SimpleOperationETNode(ETNode left, Token operator, ETNode right) {
        this.left = left.setParent(this);
        this.operator = operator;
        this.right = right.setParent(this);
    }

    public CDT execute(CraterVariableScope scope) {
        /**
         * fix for short circuiting
         */

        CDT l = this.left.executeMetaSafe(scope);
        CDT r = this.right.executeMetaSafe(scope);

        switch (this.operator.token) {
            case C_PLUS: return l.siPlus(r);
            case C_STAR: return l.siMultiply(r);
            case C_DASH: return l.siSubtract(r);
            case C_FLSASH: return l.siDivide(r);
            case C_DOUBLE_FLSASH: return l.siFloorDivide(r);
            case KW_AND: return l.siBooleanAnd(r);
            case KW_OR: return l.siBooleanOr(r);
            case KW_XOR: return l.siBooleanXor(r);
            case C_LESSTHAN: return l.siLessThan(r);
            case C_GREATERTHAN: return l.siGreaterThan(r);
            case D_DOUBLE_EQUALS: return l.siMutuallyEqualTo(r);
            case KW_CONTAINS: return l.siContains(r);
            case KW_IN: return l.siIn(r);
            case C_MOD: return l.siMod(r);
            case KW_IS: return l.siIs(r);
            case D_NOT_EQUAL: return l.siNotEqual(r);
        }

        throw new CraterExecutionException("invalid operation symbol");
    }

}
