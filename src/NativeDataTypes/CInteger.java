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

        if (other instanceof InfCDT) {
            return new CBoolean(true);
        }

        if (other instanceof NinfCDT) {
            return new CBoolean(false);
        }

        return super.siLessThan(other);
    }

    @Override
    public CDT siGreaterThan(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.intValue > other.toInt());
        }

        if (other instanceof InfCDT) {
            return new CBoolean(false);
        }

        if (other instanceof NinfCDT) {
            return new CBoolean(true);
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

    @Override
    public CDT siMod(CDT other) {
        if (other instanceof CInteger) {
            return new CInteger(this.intValue % other.toInt());
        }
        return super.siMod(other);
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

    @Override
    public int hashCode() {
        return this.intValue;
    }

    @Override
    public CDT siBooleanOr(CDT other) {
        if (other instanceof CNone) {
            return this;
        }
        return super.siBooleanOr(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CDT) {
            return this.siMutuallyEqualTo((CDT)obj).toBool();
        }
        return false;
    }
}
