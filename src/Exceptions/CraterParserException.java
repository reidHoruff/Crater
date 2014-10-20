package Exceptions;

/**
 * Created by reidhoruff on 10/7/14.
 */

public class CraterParserException extends RuntimeException {
    public CraterParserException(String msg) {
        System.out.println(msg);
        System.exit(1);
    }
}
