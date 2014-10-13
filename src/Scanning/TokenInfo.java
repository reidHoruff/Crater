package Scanning;

/**
 * Created by reidhoruff on 10/7/14.
 */

import java.util.regex.Pattern;

public class TokenInfo {
    public final Pattern regex;
    public final TokenType token;

    public TokenInfo(Pattern regex, TokenType token) {
        super();
        this.regex = regex;
        this.token = token;
    }
}

