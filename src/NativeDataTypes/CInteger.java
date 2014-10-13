package NativeDataTypes;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class CInteger extends CDT {
    public int intValue;

    public CInteger(int value) {
        super();
        this.setIntValue(value);
    }

    public void setIntValue(int value) {
        this.intValue = value;
    }

    /*
    simple operations with other CDTs
     */

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.intValue == other.toInt());
        }
        return super.siMutuallyEqualTo(other);
    }

    @Override
    public CDT siMultiply(CDT other) {
        if (other instanceof CInteger) {
            return new CInteger(this.intValue * other.toInt());
        }
        return super.siMultiply(other);
    }

    @Override
    public CDT siLessThan(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.intValue < other.toInt());
        }
        return super.siLessThan(other);
    }

    @Override
    public CDT siSubtract(CDT other) {
        if (other instanceof CInteger) {
            return new CInteger(this.intValue - other.toInt());
        }
        return super.siSubtract(other);
    }

    @Override
    public CDT siDivide(CDT other) {
        if (other instanceof CInteger) {
            return new CInteger(this.intValue / other.toInt());
        }
        return super.siDivide(other);
    }

    @Override
    public CDT siPlus(CDT other) {
        if (other instanceof CInteger) {
            return new CInteger(this.intValue + other.toInt());
        }
        return super.siPlus(other);
    }
/*
    end simple operations with other CDTs
     */

    @Override
    public String toString() {
        return Integer.toString(this.intValue);
    }

    @Override
    public String getTypeName() {
        return "int";
    }
}
