import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Tokenizer tkz;

    public Parser(Tokenizer tkz) {
        this.tkz = tkz;
    }

    private AST parseE() {
        AST ast = parseT();
        while (tkz.peek("+") || tkz.peek("-")) {
            ast = new Grammar.E(ast, tkz.consume(), parseT());
        }
        return ast;
    }

    private AST parseT() {
        AST ast = parseF();
        while (tkz.peek("*") || tkz.peek("/") || tkz.peek("%")) {
            ast = new Grammar.T(ast, tkz.consume(), parseF());
        }
        return ast;
    }


    private Grammar.F parseF() {
        String value = tkz.peek();
        Matcher matcher = Pattern.compile("^\\d+|\\w+$").matcher(value);
        if (matcher.find()) {
            return new Grammar.F(tkz.consume());
        } else {
            tkz.consume("(");
            Grammar.F f = new Grammar.F(parseE());
            tkz.consume(")");
            return f;
        }
    }

    public AST parseAST() {
        return parseE();
    }
}
