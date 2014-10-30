package ExecutionTree;

import CraterExecutionEnvironment.CExecSingleton;
import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.*;
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
        this.cclass.setConstructor(constructor);
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        this.cclass.setStaticScope(scope.extend());
        return this.cclass.execute();
    }
}
