package NativeDataTypes;

/**
 * Created by reidhoruff on 10/8/14.
 */

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import Exceptions.CraterInvalidSimpleOperationException;

import java.util.ArrayList;

/**
 * Crater Data Type
 */

public abstract class CDT implements Comparable<CDT> {
    public String toString() {
        return "CDT";
    }

    public CDT clone() {
        return this;
    }

    public CDT metaSafe() {
        return this;
    }

    public MetaCDT withMetaWrapper() {
        return new MetaCDT(this.metaSafe());
    }

    public CDT callWithArguments(ArrayList<CDT> values) {
        throw new CraterExecutionException("cannot call upon this object [" + this.getTypeName() + "]");
    }

    public CDT callWithSingleArgument(CDT value) {
        throw new CraterExecutionException("cannot call upon this object [" + this.getTypeName() + "]");
    }

    /*
    To CDT Subtypes
     */
    public CInteger toCInteger() {
        return (CInteger)this;
    }

    public CFloat toCFloat() {
        return (CFloat)this;
    }

    public CString toCString() {
        return (CString)this;
    }

    public double toFloat() {
        return ((CFloat)this).value;
    }

    public CDict toCDict() {
        return (CDict)this;
    }

    public int toInt() {
        return toCInteger().value;
    }

    public CList toCList() {
        return (CList)this;
    }

    public CAtom toCAtom() {
        return (CAtom)this;
    }

    public CTuple toCTuple() {
        return (CTuple)this;
    }

    public CBoolean toCBoolean() {
        return (CBoolean)this;
    }

    public CFunction toCFunction() {
        return (CFunction)this;
    }

    public CRange toCRange() {
        return (CRange)this;
    }

    public boolean toBool() {
        return this.toCBoolean().value;
    }

    public abstract String getTypeName();

    /**
     * SIMPLE OPERATIONS...
     */
    public CDT siMutuallyEqualTo(CDT other) {
        return new CBoolean(this == other);
    }

    public CDT siNot() {
        throw new CraterInvalidSimpleOperationException("not", this);
    }

    public CDT siNotEqual(CDT other) {
        throw new CraterInvalidSimpleOperationException("!=", this, other);
    }

    public CDT siContains(CDT other) {
        throw new CraterInvalidSimpleOperationException("contains", this, other);
    }

    public CDT siPlus(CDT other) {
        throw new CraterInvalidSimpleOperationException("+", this, other);
    }

    public CDT siDivide(CDT other) {
        throw new CraterInvalidSimpleOperationException("/", this, other);
    }

    public CDT siMultiply(CDT other) {
        throw new CraterInvalidSimpleOperationException("*", this, other);
    }

    public CDT siSubtract(CDT other) {
        throw new CraterInvalidSimpleOperationException("-", this, other);
    }

    public CDT siBooleanAnd(CDT other) {
        throw new CraterInvalidSimpleOperationException("and", this, other);
    }

    public CDT siBooleanOr(CDT other) {
        throw new CraterInvalidSimpleOperationException("or", this, other);
    }

    public CDT siBooleanXor(CDT other) {
        throw new CraterInvalidSimpleOperationException("xor", this, other);
    }

    public CDT siLessThan(CDT other) {
        throw new CraterInvalidSimpleOperationException("<", this, other);
    }

    public CDT siGreaterThan(CDT other) {
        throw new CraterInvalidSimpleOperationException(">", this, other);
    }

    public CDT siLessThanOrEqual(CDT other) {
        throw new CraterInvalidSimpleOperationException("<=", this, other);
    }

    public CDT siGreaterThanOrEqual(CDT other) {
        throw new CraterInvalidSimpleOperationException(">=", this, other);
    }

    public CDT siMod(CDT other) {
        throw new CraterInvalidSimpleOperationException("%", this, other);
    }

    public CDT siPlusEquals(CDT other) {
        throw new CraterInvalidSimpleOperationException("+=", this, other);
    }

    public CDT siIs(CDT other) {
        return new CBoolean(this == other);
    }

    public CDT siIndex(CDT index) {
        throw new CraterExecutionException(this.getTypeName() + " cannot be indexed by type " + index.getTypeName());
    }

    public CDT siIn(CDT other) {
        throw new CraterInvalidSimpleOperationException("in", this, other);
    }

    public CDT siInstantiate(ArrayList<CDT> arguments) {
        throw new CraterExecutionException("[" + this.getTypeName() + "] cannot be instantiated");
    }

    public CDT siCompareTo(CDT other) {
        throw new CraterExecutionException("cannot compare [" + this.getTypeName() + "] with [" + other.getTypeName() + "]");
    }

    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        if (identifier.equals("new")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    return this.host.siInstantiate(values);
                }
            };
        }

        if (identifier.equals("id")) {
            return new CInteger(System.identityHashCode(this));
        }

        if (identifier.equals("to_s")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    return new CString(this.host.toString());
                }
            };
        }

        if (identifier.equals("put")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    System.out.println(this.host.toString());
                    return CNone.get();
                }
            };
        }

        if (identifier.equals("get_t")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    return new CString(this.host.getTypeName());
                }
            };
        }

        throw new CraterExecutionException("[" + getTypeName() + "] has no member [" + identifier + "]");
    }

    @Override
    public int compareTo(CDT other) {
        return this.siCompareTo(other).toInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CDT) {
            return this.siMutuallyEqualTo((CDT) obj).toBool();
        }
        return super.equals(obj);
    }

    /**
     * type checking
     */

    public boolean isNumber() { return false; }
}
