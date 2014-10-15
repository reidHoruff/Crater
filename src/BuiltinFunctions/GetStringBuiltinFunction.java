package BuiltinFunctions;

import ExecutionTree.FunctionDefinitionETNode;
import NativeDataTypes.CDT;
import NativeDataTypes.CFunction;
import NativeDataTypes.CString;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class GetStringBuiltinFunction extends CFunction {

    public GetStringBuiltinFunction() {
        super(null);
    }

    @Override
    public CDT callWithArguments(ArrayList<CDT> values) {
        Scanner in = new Scanner(System.in);
        return new CString(in.nextLine());
    }
}
