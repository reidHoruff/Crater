package Scanning;

/**
 * Created by reidhoruff on 10/7/14.
 */

public class Token {
    public final TokenType token;
    public final String sequence;
    public final int line;
    public final String origLine;
    public final int column;

    public Token(TokenType token, String sequence, int line, String origLine, int column) {
        super();
        this.token = token;
        this.sequence = sequence;
        this.line = line;
        this.origLine = origLine;
        this.column = column;
    }

    public String toString() {
        String caret = "";
        for (int i = 0; i < column; i++) caret += "-";
        for (int i = 0; i < this.sequence.length(); i++) caret += "^";
        return "line: " + this.line +  ": col: " + (this.column + 1) + "\n" + this.origLine + "\n" + caret;
    }
}

