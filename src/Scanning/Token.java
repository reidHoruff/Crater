package Scanning;

/**
 * Created by reidhoruff on 10/7/14.
 */

public class Token {
    public final TokenType token;
    public final String sequence;

    public Token(TokenType token, String sequence) {
        super();
        this.token = token;
        this.sequence = sequence;
    }

    public String toString() {
        return "" + this.token + " " + this.sequence;
    }
}

