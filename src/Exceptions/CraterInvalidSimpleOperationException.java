package Exceptions;

import NativeDataTypes.CDT;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class CraterInvalidSimpleOperationException extends RuntimeException{
    public CraterInvalidSimpleOperationException(String operation, CDT l, CDT r) {
        super("Cannot execute [" + l.getTypeName() + " " + operation + " " + r.getTypeName() + "], invalid operand types");
    }
}
