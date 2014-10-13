package BuiltinFunctions;

import Exceptions.CraterExecutionException;
import ExecutionTree.FunctionDefinitionETNode;
import NativeDataTypes.*;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class LengthBuiltinFunction extends CFunction {

    public LengthBuiltinFunction() {
        super(null);
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values) {
        if (values.size() != 1) {
            throw new CraterExecutionException("len() must be called with exactly 1 parameter");
        }

        if (values.get(0) instanceof CList) {
            return new CInteger(values.get(0).toCList().getLength());
        }

        if (values.get(0) instanceof CString) {
            return new CInteger(values.get(0).toString().length());
        }

        throw new CraterExecutionException("len() must be given a single list or string");
    }

}
