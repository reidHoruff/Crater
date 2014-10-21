package NativeDataTypes;

import Exceptions.CraterExecutionException;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class FinalMetaCDT extends MetaCDT {

    public FinalMetaCDT(CDT data) {
        super(data);
    }

    @Override
    public void setData(CDT data) {
        throw new CraterExecutionException("<Cannot overwrite variable declared as final>");
    }
}
