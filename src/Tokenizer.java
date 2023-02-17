import java.util.regex.Pattern;

public class Tokenizer {
    private String src, next;
    private int pos, depth;

    public Tokenizer(String src) {
        this.src = src;
        this.pos = 0;
        this.depth = 0;
        sanityCheck();
        compute();
    }

    private void sanityCheck() {
        if (Pattern.compile("\\d+ \\d+").matcher(this.src).find())
            throw new Exception.SyntaxError("no space allowed between digits");
    }

    public boolean hasNextToken() {
        return next != null;
    }

    public boolean peek(String s) {
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }

    public String peek() {
        if (!hasNextToken())
            throw new Exception.TokenError("no more token");
        return next;
    }

    public void consume(String s) {
        if (peek(s)) {
            consume();
        } else {
            throw new Exception.TokenError("no specified token");
        }
    }

    public String consume() {
        if (!hasNextToken())
            throw new Exception.TokenError("no more tokens");
        String result = next;
        compute();
        return result;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    private void processDigit(StringBuilder s) {
        s.append(src.charAt(pos));
        for (pos++; pos < src.length() && isDigit(src.charAt(pos)); pos++)
            s.append(src.charAt(pos));
    }

    private void processCharacter(StringBuilder s) {
        s.append(src.charAt(pos));
        for (pos++; pos < src.length() && isCharacter(src.charAt(pos)); pos++)
            s.append(src.charAt(pos));
    }

    private void compute() {
        StringBuilder s = new StringBuilder();
        while (pos < src.length() && src.charAt(pos) == ' ')
            pos++;
        if (pos == src.length()) {
            if (depth != 0) throw new Exception.SyntaxError("illegal parentheses");
            next = null;
            return;
        }
        char c = src.charAt(pos);
        if (isDigit(c)) {
            processDigit(s);
        } else if (isCharacter(c)) {
            processCharacter(s);
        } else if (c == '(') {
            s.append(c);
            depth++;
            pos++;
        } else if (c == ')') {
            if (depth <= 0) throw new Exception.SyntaxError("illegal parentheses");
            s.append(c);
            depth--;
            pos++;
        } else if (isOperator(c)) {
            s.append(c);
            pos++;
        } else {
            throw new Exception.SyntaxError("lexical error");
        }
        next = s.toString();
    }
}
