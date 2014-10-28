package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import NativeDataTypes.CRange;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class CRangeETNode extends ETNode {

    public ETNode head, tail, increment;

    public CRangeETNode(ETNode head, ETNode tail, ETNode increment) {
        this.head = head.setParent(this);
        this.tail = tail.setParent(this);
        this.increment = increment.setParent(this);
    }

    public CRangeETNode(ETNode head, ETNode tail) {
        this(head, tail, new IntegerLiteralETNode(1));
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        CDT head = this.head.executeMetaSafe(scope);
        CDT tail = this.tail.executeMetaSafe(scope);
        CDT increment = this.increment.executeMetaSafe(scope);


        if (!(head instanceof CInteger)) {

        }

        if (!(tail instanceof CInteger)) {

        }

        if (!(increment instanceof CInteger)) {

        }

        return new CRange(head.toInt(), tail.toInt(), increment.toInt());
    }
}
