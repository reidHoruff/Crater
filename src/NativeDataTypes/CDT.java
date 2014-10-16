package NativeDataTypes;

/**
 * Created by reidhoruff on 10/8/14.
 */

import Exceptions.CraterExecutionException;
import Exceptions.CraterInvalidSimpleOperationException;

import java.util.ArrayList;

/**
 * Crater Data Type
 */

public abstract class CDT {
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
        return new MetaCDT(this);
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

    public CDict toCDict() {
        return (CDict)this;
    }

    public int toInt() {
        return toCInteger().intValue;
    }

    public CList toCList() {
        return (CList)this;
    }

    public CAtom toCAtom() {
        return (CAtom)this;
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

    public CDT siIs(CDT other) {
        return this.siMutuallyEqualTo(other);
    }

    public CDT siIndex(CDT index) {
        throw new CraterExecutionException(this.getTypeName() + " cannot be indexed by type " + index.getTypeName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CDT) {
            return this.siMutuallyEqualTo((CDT) obj).toBool();
        }
        return super.equals(obj);
    }
}
