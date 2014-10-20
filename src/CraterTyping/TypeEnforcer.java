package CraterTyping;

/**
 * Created by reidhoruff on 10/19/14.
 */

import Exceptions.CraterTypingException;
import NativeDataTypes.*;

/**
 * non nested typing
 */
public class TypeEnforcer {

    private CType type;

    public TypeEnforcer(CType type) {
        this.type = type;
    }

    public boolean isCorrectType(CDT data) {
        data = data.metaSafe();
        switch (this.type) {
            case INT:
            case INF:
            case NINF:
                return data instanceof CInteger;
            case BOOLEAN: return data instanceof CBoolean;
            case STRING: return data instanceof CString;
            case LIST: return data instanceof CList;
            case FUNCTION: return data instanceof CFunction;
            default: return false;
        }
    }

    public CraterTypingException getException(CDT data) {
        return new CraterTypingException("Expected type of " + this.type.toString()
                + " but received type of " + data.getTypeName());
    }

}
