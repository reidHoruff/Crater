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

    public void setChildrenVariableScope(CraterVariableScope scope) {
        for (ETNode child : this.children) {
            child.setVariableScope(scope);
        }
    }

    public void add(ETNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public CDT execute() {
        CTuple tuple = new CTuple();
        for (ETNode child : this.children) {
            tuple.addCDT(child.execute());
        }
        return tuple;
    }
}
