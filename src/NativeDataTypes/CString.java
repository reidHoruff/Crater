package NativeDataTypes;

import Scanning.Token;

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
