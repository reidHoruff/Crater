package NativeDataTypes;

import CraterExecutionEnvironment.CraterVariableScope;
import ExecutionTree.ClassDefinitionETNode;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/27/14.
 */
public class CClass extends CDT {
    private ClassDefinitionETNode classETNode;
    private CraterVariableScope staticScope;

    public CClass(ClassDefinitionETNode classETNode, CraterVariableScope scope) {
        this.classETNode = classETNode;
        this.staticScope = scope;
        /**
         * set static keyword to point to class reference
         */
        this.staticScope.nonRecursiveSetValue("static", this.withMetaWrapper());
    }

    @Override
    public CDT siAccessMember(String identifier) {
        MetaCDT cdt = this.staticScope.getVariableReference(identifier);
        return cdt;
    }

    @Override
    public String getTypeName() {
        return "class";
    }

    @Override
    public String toString() {
        return "class";
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values) {
        this.classETNode.constructor.
    }
}
