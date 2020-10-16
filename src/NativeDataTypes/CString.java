package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
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
            return new CBoolean((this.value.equals(other.toCString().value)));
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
    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        if (identifier.equals("length")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    return new CInteger(((CString)this.host).length());
                }
            };
        }

        return super.siAccessMember(identifier, accessor);
    }

    @Override
    public CDT siMultiply(CDT other) {
        if (other instanceof CInteger) {
            StringBuilder builder = new StringBuilder();

            long times = other.toInt();

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

    public String toStringWithQuotes() {
        return "\"" + this.toString() + "\"";
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

    @Override
    public CDT siIndex(CDT index) {
        if (index instanceof CInteger) {
            int idx = (int)index.toInt();

            if (idx < 0 || idx >= this.value.length()) {
                throw new CraterExecutionException("String index out of bounds.");
            }

            return new CString(this.value.substring(idx, idx + 1));
        }

        else if (index instanceof CRange) {

            CRange range = index.toCRange();

            if (range.isLazy()) {
                range.lazyRealize(new CInteger(this.length() - 1));
            }

            ArrayList<MetaCDT> values = range.generateList().getItems();
            String out = ""; // TODO: should be string builder, will be slow

            for (MetaCDT mcidx : values) {
                CDT cidx = mcidx.metaSafe();
                if (!(cidx instanceof CInteger)) {
                    throw new CraterExecutionException("String index range contains non integer value.");
                }

                int idx = (int)cidx.toInt();

                if (idx < 0 || idx >= this.value.length()) {
                    throw new CraterExecutionException("String range index out of bounds.");
                }

                out += this.value.substring(idx, idx + 1);
            }

            return new CString(out);
        }

        throw new CraterExecutionException(this.getTypeName() + " cannot be indexed by type " + index.getTypeName());
    }

}
