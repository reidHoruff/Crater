package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import NativeDataTypes.CNone;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/8/14.
 */

public class StatementListETNode extends ETNode {
    private ArrayList<ETNode> children;

    public StatementListETNode() {
        this.children = new ArrayList<ETNode>();
    }

    public void add(ETNode child) {
        if (child != null) {
            child.setParent(this);
            this.children.add(child);
        }
    }

    public void setChildrenVariableScope(CraterVariableScope scope) {
        for (ETNode child : this.children) {
            child.setVariableScope(getVariableScope());
        }
    }

    public void setVariableScope(CraterVariableScope scope) {
        this.variableScope = null;
        if (this.parent != null) {
            this.variableScope = new CraterVariableScope(this.parent.getVariableScope());
        } else {
            this.variableScope = scope;
        }

        this.setChildrenVariableScope(this.variableScope);
    }

    public CDT execute() {
        CDT lastValue = null;

        for (ETNode child : this.children) {
            lastValue = child.execute();
        }

        if (lastValue != null) {
            return lastValue;
        } else {
            return new CNone();
        }
    }

    public void print(int level) {
        for (ETNode child : this.children) {
            putSpaces(level);
            child.print(level + 1);
        }
    }
}
