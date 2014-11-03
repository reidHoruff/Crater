package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;

import java.util.ArrayList;

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
    public CDT siCompareTo(CDT other) {
        if (other instanceof CInteger) {
            return new CInteger(this.intValue - other.toInt());
        }
        return super.siCompareTo(other);
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
    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        if (identifier.equals("each")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    for (CDT value : values) {
                        int v = ((CInteger)this.host).intValue;
                        for (int i = 0; i < v; i++) {
                            value.callWithSingleArgument(new CInteger(i));
                        }
                    }
                    return CNone.get();
                }
            };
        }

        if (identifier.equals("_inc")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    ((CInteger)this.host).increment();
                    return this.host;
                }
            };
        }

        if (identifier.equals("dec")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    ((CInteger)this.host).deincrement();
                    return this.host;
                }
            };
        }
        return super.siAccessMember(identifier, accessor);
    }

    public void increment() {
        this.intValue += 1;
    }

    public void deincrement() {
        this.intValue -= 1;
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
    public CDT siPlusEquals(CDT other) {
        if (other instanceof CInteger) {
            this.intValue += other.toInt();
            return this;
        }
        return super.siPlusEquals(other);
    }

    @Override
    public CDT siMod(CDT other) {
        if (other instanceof CInteger) {
            return new CInteger(this.intValue % other.toInt());
        }
        return super.siMod(other);
    }

    @Override
    public CDT siNotEqual(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.intValue != other.toInt());
        }
        return super.siNotEqual(other);
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
        return "integer";
    }

    @Override
    public int hashCode() {
        return this.intValue;
    }

    @Override
    public CDT siContains(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.intValue % ((CInteger) other).intValue == 0);
        }

        return super.siContains(other);
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

    /**
    @Override
    public CDT siIs(CDT other) {
        return this.siMutuallyEqualTo(other);
    }
    */
}
