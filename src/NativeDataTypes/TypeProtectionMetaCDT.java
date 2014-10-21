package NativeDataTypes;

import CraterTyping.CType;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class TypeProtectionMetaCDT extends MetaCDT {

    public final CType expectedType;

    public TypeProtectionMetaCDT(CType expectedType) {
        this.expectedType = expectedType;
    }

    @Override
    public void setData(CDT data) {
        super.setData(data);
    }
}
