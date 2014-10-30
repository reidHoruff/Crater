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

    public void addFunction(ETNode child) {
        this.cclass.addFunction(child.setParent(this));
    }

    public void addVariable(ETNode child) {
        this.cclass.addVariable(child.setParent(this));
    }

    public void setConstructor(ETNode constructor) {
        this.cclass.setConstructorDefinition(constructor);
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        this.cclass.setStaticScope(scope.extend());
        return this.cclass.execute();
    }
}
