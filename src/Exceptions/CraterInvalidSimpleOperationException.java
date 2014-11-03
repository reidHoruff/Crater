package Exceptions;

import CraterExecutionEnvironment.CExecSingleton;
import NativeDataTypes.CDT;

import java.util.Stack;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class CraterInvalidSimpleOperationException extends CraterExecutionException{

    public CraterInvalidSimpleOperationException(String operation, CDT l, CDT r) {
        super("<Cannot Execute [" + l.getTypeName() + " " + operation + " " + r.getTypeName() + "] - Invalid Operand Types>");
    }

    public CraterInvalidSimpleOperationException(String operation, CDT r) {
        super("<Cannot Execute "  + operation + " [" + r.getTypeName() + "] - Invalid Operand Type>");
    }
}
