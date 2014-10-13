package NativeDataTypes;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class MetaCDT extends CDT {
    private CDT data;

    public MetaCDT(CDT data) {
        this.data = data;
    }

    public MetaCDT() {
        this.data = new CNone();
    }

    public CDT metaSafe() {
        return this.data.metaSafe();
    }

    public void setData(CDT data) {
        if (data instanceof MetaCDT) {
            System.err.println("Warning: nested MetaCDT");
        }
        this.data = data;
    }

    public String toString() {
        return data.toString();
    }

    public CDT clone() {
        return new MetaCDT(this.data.clone());
    }

    public String getTypeName() {
        return "meta(" + this.data.getTypeName() + ")";
    }

    @Override
    public CDT nonrecursiveMetaSafe() {
        return this.data;
    }

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        return this.metaSafe().siMutuallyEqualTo(other.metaSafe());
    }
}
