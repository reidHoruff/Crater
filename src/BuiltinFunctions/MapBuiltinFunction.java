package BuiltinFunctions;

import Exceptions.CraterExecutionException;
import ExecutionTree.ETNode;
import NativeDataTypes.*;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class MapBuiltinFunction extends CFunction {

    public MapBuiltinFunction() {
        super();
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
        if (values.size() != 2) {
            throw new CraterExecutionException("map() must be called with exactly 2 parameters");
        }

        if (values.get(0) instanceof CFunction) {
            CFunction lambda = values.get(0).toCFunction();
            if (values.get(1) instanceof CList) {
                return this.mapToList(lambda, values.get(1).toCList(), parent);
            } else if (values.get(1) instanceof CRange) {
                return this.mapToRange(lambda, values.get(1).toCRange(), parent);
            }
        }

        throw new CraterExecutionException("map(function, list) given invalid arguments");
    }

    private CDT mapToList(CFunction lambda, CList list, ETNode parent) {
        CList newList = new CList();

        for (int i = 0; i < list.getLength(); i++) {
            newList.addCDT(lambda.callWithSingleArgument(list.getElementMetaSafe(i), parent));
        }

        return newList;
    }

    private CDT mapToRange(CFunction lambda, CRange range, ETNode parent) {
        CList list = new CList();

        /*
        for (int i = range.head; i < range.tail; i += range.increment) {
            list.addCDT(lambda.callWithSingleArgument(new CInteger(i), parent));
        }
        */

        return list;
    }
}
