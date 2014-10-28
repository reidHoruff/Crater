package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CList;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class ListLiteralETNode extends ETNode{

    private ArrayList<ETNode> children;

    public ListLiteralETNode() {
        super();
        this.children = new ArrayList<ETNode>();
    }

    public void add(ETNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public CDT execute(CraterVariableScope scope) {
        CList list = new CList();
        for (ETNode child : this.children) {
            list.addCDT(child.executeMetaSafe(scope));
        }
        return list;
    }

}
