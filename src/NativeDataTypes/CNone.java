package NativeDataTypes;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class CNone extends CDT{
    public String toString() {
        return "none";
    }

    public String getTypeName() {
        return "none";
    }

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        return new CBoolean(other.metaSafe() instanceof CNone);
    }
}
