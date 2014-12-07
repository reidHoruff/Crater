/**
 * Created by reidhoruff on 10/6/14.
 */

import CraterExecutionEnvironment.CExecSingleton;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import Parsing.CraterParser;
import Scanning.*;

import java.io.File;
import java.io.FileNotFoundException;

public class CraterEntry {
    public static void main(String args[]) {
        /*
        setup is kind of ugly/hacky right now...
         */
        
        String name = "test2.cr";

        if (args.length >= 1) {
            name = args[0];
        }

        try {
            CraterTokenizer tokenizer = new CraterTokenizer(new File(name));
            new CraterParser(tokenizer);
            CExecSingleton.get().executeProgram();
        } catch (FileNotFoundException e) {
            System.err.println("<source file not found>");
            System.exit(1);
        }
    }
}
