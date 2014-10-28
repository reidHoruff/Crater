package NativeDataTypes;

import CraterTyping.TypeEnforcer;
import Exceptions.CraterExecutionException;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class TypeProtectionMetaCDT extends MetaCDT {

    public TypeEnforcer typeEnforcer;

    public TypeProtectionMetaCDT(TypeEnforcer typeEnforcer, CDT data) {
        this.typeEnforcer = typeEnforcer;
        this.setData(data);
    }

    @Override
    public void setData(CDT data) {
        if (this.typeEnforcer.isCorrectType(data)) {
            super.setData(data);
        } else {
            throw this.typeEnforcer.getException(data);
        }
    }
}
