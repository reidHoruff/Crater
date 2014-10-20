package Exceptions;

/**
 * Created by reidhoruff on 10/8/14.
 */

public class CraterExecutionException extends RuntimeException {
    public CraterExecutionException(String msg) {
        System.exit(1);
        System.out.println(msg);
    }
}
