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

    @Override
    protected void handleBreak() {
        this.isExecutionBroken = true;
        super.handleBreak();
    }

    @Override
    protected void handleReturn() {
        this.isExecutionBroken = true;
        super.handleReturn();
    }

    public CDT execute(CraterVariableScope scope) {
        CDT lastValue = null;
        this.isExecutionBroken = false;

        for (ETNode child : this.children) {
            lastValue = child.executeMetaSafe(scope);
            if (this.isExecutionBroken) {
                break;
            }
        }

        scope.clear();

        if (lastValue != null) {
            return lastValue;
        } else {
            return CNone.get();
        }
    }

    public void print(int level) {
        for (ETNode child : this.children) {
            putSpaces(level);
            child.print(level + 1);
        }
    }
}
