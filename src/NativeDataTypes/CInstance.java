package NativeDataTypes;

import CraterExecutionEnvironment.CraterVariableScope;

/**
 * Created by reidhoruff on 10/27/14.
 */
public class CInstance extends CDT {

    private CClass baseClass;
    private CraterVariableScope instanceScope;

    public CInstance(CClass baseClass) {
        this.baseClass = baseClass;
        this.instanceScope = baseClass.getStaticScope().extend();

        this.baseClass.executeOnScope(this.instanceScope);

        this.instanceScope.nonRecursiveSetValue("static", baseClass.withMetaWrapper().setFinal(true));
        this.instanceScope.nonRecursiveSetValue("this", this.withMetaWrapper().setFinal(true));
    }

    @Override
    public String getTypeName() {
        return this.baseClass.getClassName() + " instance";
    }

    @Override
    public String toString() {
        return this.baseClass.getClassName() + " instance";
    }

    @Override
    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        if (this.instanceScope.recursiveHasVariable(identifier)) {
            return this.instanceScope.getVariableReference(identifier);
        }

        return super.siAccessMember(identifier, accessor);
    }
}
