/**
 * Created by reidhoruff on 10/6/14.
 */

import CraterExecutionEnvironment.CExecSingleton;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import Parsing.CraterParser;
import Scanning.*;

import java.io.File;

public class CraterEntry {
    public static void main(String args[]) {
        CraterTokenizer tokenizer = new CraterTokenizer(new File("test2.cr"));
        new CraterParser(tokenizer);
        CExecSingleton.get().executeProgram();
    }
}
