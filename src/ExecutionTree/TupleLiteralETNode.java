package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CTuple;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class TupleLiteralETNode extends ETNode {

    private ArrayList<ETNode> children;

    public TupleLiteralETNode() {
        super();
        this.children = new ArrayList<ETNode>();
    }

    public void add(ETNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public CDT execute(CraterVariableScope scope) {
        CTuple tuple = new CTuple();
        for (ETNode child : this.children) {
            tuple.addCDT(child.executeMetaSafe(scope));
        }
        return tuple;
    }
}
