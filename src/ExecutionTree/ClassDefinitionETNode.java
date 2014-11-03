package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.*;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/27/14.
 */
public class ClassDefinitionETNode extends ETNode {

    private ArrayList<ETNode> children;
    private ETNode constructor;
    private boolean isExecutionBroken = false;
    private CClass cclass;

    public ClassDefinitionETNode(String className) {
        this.cclass = new CClass(className);
    }

    public CClass getCClass() {
        return this.cclass;
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        this.cclass.setStaticScope(scope.extend());
        return this.cclass.execute();
    }
}
