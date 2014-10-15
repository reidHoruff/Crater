package BuiltinFunctions;

import NativeDataTypes.*;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class ListBuiltinFunction extends CFunction {
    public ListBuiltinFunction() {
        super(null);
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values) {
        if (values.get(0) instanceof CRange) {
            return values.get(0).toCRange().generateList();
        }
        return new CNone();
    }
}
