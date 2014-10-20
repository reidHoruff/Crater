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
        CraterTokenizer tokenizer = new CraterTokenizer(new File("test.cr"));
        new CraterParser(tokenizer);
        CraterExecutionEnvironmentSingleton.getInstance().executeProgram();
    }
}
