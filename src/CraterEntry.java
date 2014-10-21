/**
 * Created by reidhoruff on 10/6/14.
 */

import CraterExecutionEnvironment.CExecSingleton;
import Parsing.CraterParser;
import Scanning.*;

import java.io.File;

public class CraterEntry {
    public static void main(String args[]) {
        CraterTokenizer tokenizer = new CraterTokenizer(new File("test.cr"));
        new CraterParser(tokenizer);
        CExecSingleton.get().executeProgram();
    }
}
