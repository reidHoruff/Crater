package BuiltinFunctions;

import Exceptions.CraterExecutionException;
import ExecutionTree.ETNode;
import ExecutionTree.FunctionDefinitionETNode;
import NativeDataTypes.*;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/12/14.
 */
public class LengthBuiltinFunction extends CBuiltinFunction {

    @Override
    public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
        if (values.size() != 1) {
            throw new CraterExecutionException("len() must be called with exactly 1 parameter");
        }

        if (values.get(0) instanceof CList) {
            return CInteger.gimmie(values.get(0).toCList().getLength());
        }

        if (values.get(0) instanceof CTuple) {
            return CInteger.gimmie(values.get(0).toCList().getLength());
        }

        if (values.get(0) instanceof CString) {
            return CInteger.gimmie(values.get(0).toString().length());
        }

        if (values.get(0) instanceof CDict) {
            return CInteger.gimmie(values.get(0).toCDict().size());
        }

        if (values.get(0) instanceof CRange) {
            return CInteger.gimmie(values.get(0).toCRange().length());
        }

        throw new CraterExecutionException("len() must be given a single list or string");
    }
}
