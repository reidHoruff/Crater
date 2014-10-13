package NativeDataTypes;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class CBoolean extends CDT {
    public boolean value;

    public CBoolean(boolean value) {
        super();
        this.value = value;
    }

    @Override
    public CDT clone() {
        return new CBoolean(this.value);
    }

    @Override
    public String toString() {
        return Boolean.toString(this.value);
    }

    /*
    simple operations with other CDTs
     */

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        if (other instanceof CBoolean) {
            return new CBoolean(this.value == other.toBool());
        }
        return super.siMutuallyEqualTo(other);
    }

    @Override
    public CDT siBooleanAnd(CDT other) {
        if (other instanceof CBoolean) {
            return new CBoolean(this.value && other.toBool());
        }
        return super.siBooleanAnd(other);
    }

    @Override
    public CDT siBooleanOr(CDT other) {
        if (other instanceof CBoolean) {
            return new CBoolean(this.value || other.toBool());
        }
        return super.siBooleanOr(other);
    }

    @Override
    public CDT siBooleanXor(CDT other) {
        if (other instanceof CBoolean) {
            return new CBoolean(this.value ^ other.toBool());
        }
        return super.siBooleanXor(other);
    }

    /*
    end simple operations with other CDTs
     */

    @Override
    public String getTypeName() {
        return "boolean";
    }
}
