package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import NativeDataTypes.CRange;
import NativeDataTypes.EndCDT;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class CRangeETNode extends ETNode {

    public ETNode head, tail, increment;
    private boolean include;

    public CRangeETNode(ETNode head, ETNode tail, boolean include, ETNode increment) {
        this.head = head.setParent(this);
        this.tail = tail.setParent(this);
        this.include = include;
        this.increment = increment.setParent(this);
    }

    public CRangeETNode(ETNode head, ETNode tail, boolean include) {
        this(head, tail, include, new IntegerLiteralETNode(1));
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        CDT head = this.head.executeMetaSafe(scope);
        CDT tail = this.tail.executeMetaSafe(scope);
        CDT increment = this.increment.executeMetaSafe(scope);

        if (!isValidCRangeType(head) || !isValidCRangeType(tail) || !isValidCRangeType(increment)) {
            throw new CraterExecutionException("range can only contain integers and floats");
        }

        return new CRange(head, tail, increment, this.include);
    }

    private boolean isValidCRangeType(CDT node) {
        return node.isNumber() || node instanceof EndCDT;
    }
}
