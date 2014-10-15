package NativeDataTypes;

/**
 * Created by reidhoruff on 10/14/14.
 */
public class DictAssignMetaCDT extends MetaCDT {

    private CDict cdict;
    private CDT key;

    public DictAssignMetaCDT(CDict cdict, CDT key) {
        this.cdict = cdict;
        this.key = key;
    }

    @Override
    public void setData(CDT data) {
        this.cdict.put(this.key, data);
        this.data = data;
    }
}
