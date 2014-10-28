package ExecutionTree;

import CraterExecutionEnvironment.CExecSingleton;
import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CClass;
import NativeDataTypes.CDT;
import NativeDataTypes.CInstance;
import NativeDataTypes.MetaCDT;
import Scanning.Token;
import com.sun.org.apache.xerces.internal.impl.dv.xs.IDDV;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/27/14.
 */
public class ClassDefinitionETNode extends ETNode {

    private ArrayList<ETNode> children;
    private ETNode constructor;
    private boolean isExecutionBroken = false;
    private CraterVariableScope staticScope;

    public ClassDefinitionETNode() {
        this.children = new ArrayList<ETNode>();
    }

    public void add(ETNode child) {
        if (child != null) {
            child.setParent(this);
            this.children.add(child);
        }
    }

    public void setConstructor(ETNode constructor) {
        this.constructor = constructor.setParent(this);
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        this.staticScope = new CraterVariableScope(CExecSingleton.get().getRootVariableScope());

        for (ETNode member: this.children) {
            member.executeMetaSafe(this.staticScope);
        }

        return new CClass(this, this.staticScope);
    }

    public CInstance construct(ArrayList<CDT> arguments) {
        
    }
}
