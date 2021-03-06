package Scanning;

/**
 * Created by reidhoruff on 10/7/14.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * This is an implementation of Scanning specific to Crater
 */
public class CraterTokenizer {
    private Tokenizer tokenizer = null;
    private ArrayList<String> lines;

    public CraterTokenizer(File input) throws FileNotFoundException {
        this.tokenizer = new Tokenizer();
        Scanner in = new Scanner(new FileReader(input));

        lines = new ArrayList<String>();

        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        this.defineTokens();
    }

    private void defineTokens() {
        //add("\\{\\-.*\\-\\}", TokenType.R_COMMENT);
        add("int", TokenType.KW_INT);
        add("not", TokenType.KW_NOT);
        add("class", TokenType.KW_CLASS);
        add("this", TokenType.KW_THIS);
        add("static", TokenType.KW_STATIC);
        add("private", TokenType.KW_PRIVATE);
        add("new", TokenType.KW_NEW);
        add("bool", TokenType.KW_BOOL);
        add("list", TokenType.KW_LIST);
        add("true", TokenType.KW_TRUE);
        add("inf", TokenType.KW_INF);
        add("var", TokenType.KW_VAR);
        add("final", TokenType.KW_FINAL);
        add("ninf", TokenType.KW_NINF);
        add("ret(urn)?", TokenType.KW_RETURN);
        add("break", TokenType.KW_BREAK);
        add("if", TokenType.KW_IF);
        add("in", TokenType.KW_IN);
        add("is", TokenType.KW_IS);
        add("by", TokenType.KW_BY);
        add("el(se)?", TokenType.KW_ELSE);
        add("for", TokenType.KW_FOR);
        add("while", TokenType.KW_WHILE);
        add("none", TokenType.KW_NONE);
        add("fun", TokenType.KW_FUN);
        add("false", TokenType.KW_FALSE);
        add("and", TokenType.KW_AND);
        add("end", TokenType.KW_END);
        add("contains", TokenType.KW_CONTAINS);
        add("or", TokenType.KW_OR);
        add("xor", TokenType.KW_XOR);
        add("\"[a-zA-Z_ 0-9\\.]*\"", TokenType.R_STRING_LITERAL);
        add("(([1-9][0-9]*)|0)\\.[0-9]+", TokenType.R_FLOAT);
        add("-?[0-9]+", TokenType.R_INT);
        add("\\.\\.\\.", TokenType.D_THREE_DOTS);
        add("\\.\\.", TokenType.D_TWO_DOTS);
        add("\\.", TokenType.C_PERIOD);
        add("\\?", TokenType.C_QUESTION);
        add("\\+=", TokenType.D_PLUS_EQUALS);
        add("\\->", TokenType.D_DASH_GT);
        add("\\%", TokenType.C_MOD);
        add("\\(", TokenType.C_LPAREN);
        add("\\[", TokenType.C_LBRACKSQ);
        add("\\]", TokenType.C_RBRACKSQ);
        add("\\,", TokenType.C_COMMA);
        add("\\)", TokenType.C_RPAREN);
        add("\\+", TokenType.C_PLUS);
        add("\\-", TokenType.C_DASH);
        add("\\*", TokenType.C_STAR);
        add("\\/\\/", TokenType.C_DOUBLE_FLSASH);
        add("\\/", TokenType.C_FLSASH);
        add("\\|", TokenType.C_PIPE);
        add("==", TokenType.D_DOUBLE_EQUALS);
        add("!=", TokenType.D_NOT_EQUAL);
        add("=", TokenType.C_EQUALS);
        add(";", TokenType.C_SEMICOL);
        add("::", TokenType.D_DBL_COLON);
        add(":", TokenType.C_COLON);
        add("\\{", TokenType.C_LCURLEY);
        add("\\}", TokenType.C_RCURLEY);
        add("\\<=", TokenType.C_LESSTHANEQ);
        add("\\<", TokenType.C_LESSTHAN);
        add("\\>=", TokenType.C_GREATERTHANEQ);
        add("\\>", TokenType.C_GREATERTHAN);
        add("[a-zA-Z_][a-zA-Z0-9_]*", TokenType.R_IDENT);
        add("\\@[a-zA-Z][a-zA-Z0-9_]*", TokenType.R_ATOM);
    }

    private void add(String reg, TokenType tokenType) {
        this.tokenizer.add(reg, tokenType);
    }

    public LinkedList<Token> getTokens() {
        this.tokenizer.tokenize(this.lines);
        return this.tokenizer.getTokens();
    }
}
