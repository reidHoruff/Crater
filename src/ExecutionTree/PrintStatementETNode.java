package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;

/**
 * DEPRICATED
 */

/**
 * Created by reidhoruff on 10/10/14.
 */
public class PrintStatementETNode extends ETNode {

    private ETNode toPrint = null;

    public PrintStatementETNode(ETNode expression) {
        super();
        this.toPrint = expression.setParent(this);
    }
    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.toPrint.setVariableScope(getVariableScope());
    }

    @Override
    public CDT execute() {
        CDT returnValue = this.toPrint.executeMetaSafe();
        System.out.println("::" + returnValue.toString());
        return returnValue;
    }
}
