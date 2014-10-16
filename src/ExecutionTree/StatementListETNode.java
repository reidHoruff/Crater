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
    private boolean isExecutionBroken = false;

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
        // he has his own variable scope that extends onto its parent's
        this.variableScope = new CraterVariableScope(scope);
        this.setChildrenVariableScope(this.variableScope);
    }

    @Override
    protected void handleBreak() {
        this.isExecutionBroken = true;
        super.handleBreak();
    }

    public CDT execute() {
        CDT lastValue = null;
        this.isExecutionBroken = false;

        for (ETNode child : this.children) {
            lastValue = child.execute();
            if (this.isExecutionBroken) {
                break;
            }
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
