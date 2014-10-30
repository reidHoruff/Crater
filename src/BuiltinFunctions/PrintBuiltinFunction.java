package BuiltinFunctions;

import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import NativeDataTypes.CFunction;
import NativeDataTypes.CList;
import NativeDataTypes.CNone;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class PrintBuiltinFunction extends CBuiltinFunction {

    private boolean newLine;

    public PrintBuiltinFunction(boolean newLine) {
        this.newLine = newLine;
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values) {
        System.out.print("::");

        for (int i = 0; i < values.size(); i++) {
            System.out.print(values.get(i).toString());

            if (i < values.size() - 1) {
                System.out.print(", ");
            }
        }

        if (this.newLine) {
            System.out.println();
        }

        return CNone.get();
    }
}
