package BuiltinFunctions;

import ExecutionTree.ETNode;
import NativeDataTypes.*;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class ListBuiltinFunction extends CFunction {
    public ListBuiltinFunction() {
        super();
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
        if (values.get(0) instanceof CRange) {
            return values.get(0).toCRange().generateList();
        }
        return CNone.get();
    }
}
