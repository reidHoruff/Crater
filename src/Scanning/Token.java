package Scanning;

/**
 * Created by reidhoruff on 10/7/14.
 */

public class Token {
    public final TokenType token;
    public final String sequence;
    public final int line;

    public Token(TokenType token, String sequence, int line) {
        super();
        this.token = token;
        this.sequence = sequence;
        this.line = line;
    }

    public String toString() {
        return "" + this.token + " " + this.sequence + " " + this.line;
    }
}

