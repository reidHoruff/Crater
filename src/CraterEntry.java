/**
 * Created by reidhoruff on 10/6/14.
 */

import CraterExecutionEnvironment.CraterExecutionEnvironmentSingleton;
import Parsing.CraterParser;
import Scanning.*;
import Exceptions.*;

import java.io.File;
import java.io.FileReader;

public class CraterEntry {
    public static void main(String args[]) {
        System.out.println("Crater...\n\n");

        CraterTokenizer tokenizer = new CraterTokenizer(new File("test.cr"));
        new CraterParser(tokenizer);
        System.out.println("\nparsing complete---\n");
        CraterExecutionEnvironmentSingleton.getInstance().executeProgram();

        /**
        System.out.println(
                CraterExecutionEnvironmentSingleton
                        .getInstance()
                        .getRootStatementList()
                        .getVariableScope()
                        .getVariableReference("foo", false));
         */
    }
}
