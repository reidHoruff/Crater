package NativeDataTypes;

import Exceptions.CraterExecutionException;

/**
 * Created by reidhoruff on 11/3/14.
 */
public class CFloat extends CDT {

    public double value;

    public CFloat(double value) {
        this.value = value;
    }

    @Override
    public String getTypeName() {
        return "float";
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }

    @Override
    public CDT siPlus(CDT other) {
        if (other instanceof CFloat) {
            return new CFloat(this.value + other.toFloat());
        }

        if (other instanceof CInteger) {
            return new CFloat(this.value + other.toInt());
        }

        return super.siPlus(other);
    }

    @Override
    public CDT siDivide(CDT other) {
        if (other instanceof CInteger) {
            long bottom = other.toInt();
            if (bottom == 0) {
                throw new CraterExecutionException("Divide by zero.");
            }
            return new CFloat(this.value / bottom);
        }
        if (other instanceof CFloat) {
            double bottom = other.toFloat();
            if (bottom == 0.0) {
                throw new CraterExecutionException("Divide by zero.");
            }
            return new CFloat(this.value / bottom);
        }
        return super.siDivide(other);
    }

    @Override
    public CDT siSubtract(CDT other) {
        if (other instanceof CInteger) {
            return new CFloat(this.value - other.toInt());
        }
        if (other instanceof CFloat) {
            return new CFloat(this.value - other.toFloat());
        }
        return super.siSubtract(other);
    }

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        if (other instanceof CFloat) {
            return new CBoolean(this.value == other.toFloat());
        }
        if (other instanceof CInteger) {
            return new CBoolean(this.value == other.toInt());
        }
        return super.siMutuallyEqualTo(other);
    }

    @Override
    public CDT siMultiply(CDT other) {
        if (other instanceof CFloat) {
            return new CFloat(this.value * other.toFloat());
        }
        if (other instanceof CInteger) {
            return new CFloat(this.value * other.toInt());
        }
        return super.siMultiply(other);
    }

    @Override
    public CDT siCompareTo(CDT other) {
        if (other instanceof CInteger) {
            return CInteger.gimmie((int)this.value - other.toInt());
        }
        if (other instanceof CFloat) {
            return CInteger.gimmie((int)(this.value - other.toFloat()));
        }
        return super.siCompareTo(other);
    }

    @Override
    public CDT siLessThan(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.value < other.toInt());
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
            return new CBoolean(this.value > other.toInt());
        }
        if (other instanceof CFloat) {
            return new CBoolean(this.value > other.toFloat());
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
    public CDT siLessThanOrEqual(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.value <= other.toInt());
        }
        if (other instanceof CFloat) {
            return new CBoolean(this.value <= other.toFloat());
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
    public CDT siGreaterThanOrEqual(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.value >= other.toInt());
        }
        if (other instanceof CFloat) {
            return new CBoolean(this.value >= other.toFloat());
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
    public CDT siPlusEquals(CDT other) {
        if (other instanceof CFloat) {
            this.value += other.toFloat();
            return this;
        }

        if (other instanceof CInteger) {
            this.value += other.toInt();
            return this;
        }

        return super.siPlusEquals(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  CFloat) {
            return this.value == ((CFloat)obj).value;
        }
        return false;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public CDT clone() {
        return new CFloat(this.value);
    }
}
