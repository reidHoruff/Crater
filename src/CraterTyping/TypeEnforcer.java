package CraterTyping;

/**
 * Created by reidhoruff on 10/19/14.
 */

import Exceptions.CraterInternalException;
import Exceptions.CraterTypingException;
import NativeDataTypes.*;
import Scanning.Token;
import Scanning.TokenType;

/**
 * non nested typing
 */
public class TypeEnforcer {

    private CType type;

    public TypeEnforcer(CType type) {
        this.type = type;
    }

    public TypeEnforcer(Token token) {
        switch (token.token) {
            case KW_INT: this.type = CType.INT; break;
            case KW_BOOL: this.type = CType.BOOLEAN; break;
            case KW_LIST: this.type = CType.LIST; break;
            default: throw new CraterInternalException("I Don't recognize this token as a type: " + token.toString());
        }
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
        return new CraterTypingException("<Expected type of " + this.type.toString()
                + " but received type of " + data.getTypeName() + ">");
    }

}
