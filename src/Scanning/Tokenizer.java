package Scanning;

/**
 * Created by reidhoruff on 10/7/14.
 */

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

    public void tokenize(String str) {
        String s = str.trim();
        tokens.clear();
        while (!s.isEmpty()) {
            boolean match = false;
            for (TokenInfo info : tokenInfos) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;
                    String tok = m.group().trim();
                    s = m.replaceFirst("").trim();
                    tokens.add(new Token(info.token, tok));
                    break;
                }
            }
            if (!match) throw new CraterParserException("Unexpected character in input: "+s);
        }
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }
}

