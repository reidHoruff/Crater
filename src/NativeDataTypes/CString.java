package NativeDataTypes;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class CString extends CDT {
    public String value = null;

    public CString(String string) {
        this.value = this.strip(string);
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
}
