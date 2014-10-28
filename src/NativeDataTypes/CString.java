package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import Scanning.Token;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class CString extends CDT {
    public String value = null;

    public CString(String string) {
        this.value = string;
    }

    public CString(Token token) {
        this.value = this.strip(token.sequence);
    }

    public int getLength() {
        return this.value.length();
    }

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        if (other instanceof CString) {
            return new CBoolean((this.value.equals(other.toString())));
        }
        return super.siMutuallyEqualTo(other);
    }

    private String strip(String s) {
        int len = s.length();
        return s.substring(1, len-1);
    }

    @Override
    public CDT siPlus(CDT other) {
        return new CString(this.value.concat(other.toString()));
    }

    @Override
    public CDT siAccessMember(String identifier) {
        if (identifier.equals("length")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    return new CInteger(((CString)this.host).length());
                }
            };
        }

        return super.siAccessMember(identifier);
    }

    @Override
    public CDT siMultiply(CDT other) {
        if (other instanceof CInteger) {
            StringBuilder builder = new StringBuilder();

            int times = other.toInt();

            while (times --> 0) {
                builder.append(this.value);
            }

            return new CString(builder.toString());
        }

        return super.siMultiply(other);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public int length() {
        return this.value.length();
    }

    @Override
    public CDT siContains(CDT other) {
        if (other instanceof CString) {
            return new CBoolean(this.value.indexOf(other.toString()) >= 0);
        }
        return super.siContains(other);
    }

    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

}
