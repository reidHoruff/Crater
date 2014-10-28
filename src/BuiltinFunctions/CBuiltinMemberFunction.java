package BuiltinFunctions;

import NativeDataTypes.CDT;

/**
 * Created by reidhoruff on 10/27/14.
 */
public abstract class CBuiltinMemberFunction extends CBuiltinFunction {
    protected CDT host;

    public CBuiltinMemberFunction(CDT host) {
        this.host = host;
    }
}
