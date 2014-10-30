package NativeDataTypes;

import Exceptions.CraterExecutionException;

/**
 * Created by reidhoruff on 10/9/14.
 *
 * MetaCDTs are wrappers of traditional CDTs
 * and as such provide the pivotal function setData(CDT data)
 *
 * A MetaCDT is returned as the result of a left hand
 * execution ef.
 * x = y
 *
 * x = left.executeAndExpectMeta()
 * y = right.executeMetaSafe()
 *
 * x now equals a MetaCDT, a wrapper containing the actual data 'x'
 * so that we can now manipulate the data that x points to.
 *
 *
 * Think of MetaCDT's as a shovel or bucket.
 * Scooping up and holding that data, we can then change the contants of
 * the bucket...
 *
 */
public class MetaCDT extends CDT {

    protected CDT data;
    protected boolean isFinal;

    protected MetaCDT(CDT data) {
        this.data = data;
    }

    public MetaCDT() {
        this.data = new CUndefined();
    }

    public CDT metaSafe() {
        return this.data.metaSafe();
    }

    public MetaCDT setFinal(boolean isFinal) {
        this.isFinal = isFinal;
        // for chaining
        return this;
    }

    public void setData(CDT data) {
        if (this.isFinal) {
            throw new CraterExecutionException("cannot reassign final variable");
        }

        if (data instanceof MetaCDT) {
            System.err.println("Warning: nested MetaCDT");
        }
        this.data = data;
    }

    @Override
    public MetaCDT withMetaWrapper() {
        return this;
    }

    public String toString() {
        return metaSafe().toString();
    }

    public CDT clone() {
        return metaSafe().clone().withMetaWrapper();
    }

    public String getTypeName() {
        return this.metaSafe().getTypeName();
        //return "meta(" + this.data.getTypeName() + ")";
    }

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        return this.metaSafe().siMutuallyEqualTo(other.metaSafe());
    }

    @Override
    public int hashCode() {
        if (this.data == null) {
            return 0;
        } else {
            return metaSafe().hashCode();
        }
    }
}
