package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import ExecutionTree.ETNode;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class CInteger extends CDT {

    public final long value;
    private static final CInteger _n1 = new CInteger(-1);
    private static final CInteger _0 = new CInteger(0);
    private static final CInteger _1 = new CInteger(1);

    public static CInteger gimmie(long val) {
        // this is how you get fucked up bugs like your integers aren't even right.
        switch ((int)val) {
            case -1: return _n1;
            case 0: return _0;
            case 1: return _1;
            default : return new CInteger(val);
        }
    }

    private CInteger(long value) {
        super();
        this.value = value;
    }

    /*
    simple operations with other CDTs
     */

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.value == other.toInt());
        }
        if (other instanceof CFloat) {
            return new CBoolean(this.value == other.toFloat());
        }
        return super.siMutuallyEqualTo(other);
    }

    @Override
    public CDT siMultiply(CDT other) {
        if (other instanceof CInteger) {
            return CInteger.gimmie(this.value * other.toInt());
        } else if (other instanceof CFloat) {
            return new CFloat(this.value * other.toFloat());
        }
        return super.siMultiply(other);
    }

    @Override
    public CDT siCompareTo(CDT other) {
        if (other instanceof CInteger) {
            return CInteger.gimmie(this.value - other.toInt());
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
        if (other instanceof CFloat) {
            return new CBoolean(this.value < other.toFloat());
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
    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        if (identifier.equals("each")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    for (CDT value : values) {
                        int v = (int)((CInteger)this.host).value;
                        for (int i = 0; i < v; i++) {
                            value.callWithSingleArgument(CInteger.gimmie(i), parent);
                        }
                    }
                    return CNone.get();
                }
            };
        }

        return super.siAccessMember(identifier, accessor);
    }

    @Override
    public CDT siSubtract(CDT other) {
        if (other instanceof CInteger) {
            return CInteger.gimmie(this.value - other.toInt());
        }
        if (other instanceof CFloat) {
            return new CFloat(this.value - other.toFloat());
        }
        return super.siSubtract(other);
    }

    @Override
    public CDT siDivide(CDT other) {
        if (other instanceof CInteger) {
            long bottom = other.toInt();
            if (bottom == 0) {
                throw new CraterExecutionException("Divide by zero.");
            }
            return CInteger.gimmie(this.value / bottom);
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
    public CDT siPlus(CDT other) {
        if (other instanceof CInteger) {
            return CInteger.gimmie(this.value + other.toInt());
        }
        if (other instanceof CFloat) {
            return new CFloat(this.value + other.toFloat());
        }
        return super.siPlus(other);
    }

    @Override
    public CDT siMod(CDT other) {
        if (other instanceof CInteger) {
            long bottom = other.toInt();
            if (bottom == 0) {
                throw new CraterExecutionException("Divide by zero.");
            }
            return CInteger.gimmie(this.value % bottom);
        }
        return super.siMod(other);
    }

    @Override
    public CDT siNotEqual(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.value != other.toInt());
        }
        if (other instanceof CFloat) {
            return new CBoolean(this.value != other.toFloat());
        }
        return super.siNotEqual(other);
    }

    /*
    end simple operations with other CDTs
     */

    @Override
    public String toString() {
        return Long.toString(this.value);
    }

    @Override
    public String getTypeName() {
        return "integer";
    }

    @Override
    public int hashCode() {
        return (int)this.value;
    }

    @Override
    public CDT siContains(CDT other) {
        if (other instanceof CInteger) {
            return new CBoolean(this.value % ((CInteger) other).value == 0);
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

    @Override
    public CDT clone() {
        // CIntegers are singletons, this should probalye be removed
        return this;
    }

    @Override
    public boolean isNumber() {
        return true;
    }
}
