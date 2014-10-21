package NativeDataTypes;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class CNone extends CDT{

    private static CNone ourInstance = new CNone();

    private CNone() {
    }

    public static CNone get() {
        return CNone.ourInstance;
    }

    public String toString() {
        return "none";
    }

    public String getTypeName() {
        return "none";
    }

    @Override
    public CDT siBooleanOr(CDT other) {
        return other;
    }

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        return new CBoolean(other.metaSafe() instanceof CNone);
    }
}
