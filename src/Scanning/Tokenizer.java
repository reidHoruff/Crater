package Scanning;

/**
 * Created by reidhoruff on 10/7/14.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.CraterParserException;

public class Tokenizer {
    private LinkedList<TokenInfo> tokenInfos;
    private LinkedList<Token> tokens;

    public Tokenizer() {
        tokenInfos = new LinkedList<TokenInfo>();
        tokens = new LinkedList<Token>();
    }

    public void add(String regex, TokenType token) {
        tokenInfos.add(new TokenInfo(Pattern.compile("^("+regex+")"), token));
    }

    public void tokenize(ArrayList<String> lines) {
        tokens.clear();

        int lineNumber = 0;

        for (String line : lines) {
            lineNumber += 1;
            line = line.trim();
            String origLine = line;

            while (!line.isEmpty()) {
                boolean match = false;
                for (TokenInfo info : tokenInfos) {
                    Matcher m = info.regex.matcher(line);
                    if (m.find()) {
                        match = true;
                        String tok = m.group().trim();
                        tokens.add(new Token(info.token, tok, lineNumber, origLine, origLine.length() - line.length()));
                        line = m.replaceFirst("").trim();
                        break;
                    }
                }

                if (!match) {
                    throw err(lineNumber, origLine, origLine.length() - line.length());
                }
            }
        }
    }

    public CraterParserException err(int lineNumber, String line, int column) {
        String caret = "";
        for (int i = 0; i < column; i++) caret += "-";
        caret += "^";

         return new CraterParserException(
                "Parse error on line: " + lineNumber + ": col: " + (column + 1) + "\n" +
                line + "\n" +
                caret
        );
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }
}

