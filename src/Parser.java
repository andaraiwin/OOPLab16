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
            ast = ArithExprFactory.getInstance().newArithmetic(ast, tkz.consume(), parseT());
        }
        return ast;
    }

    private AST parseT() {
        AST ast = parseF();
        while (tkz.peek("*") || tkz.peek("/") || tkz.peek("%")) {
            ast = ArithExprFactory.getInstance().newArithmetic(ast, tkz.consume(), parseF());
        }
        return ast;
    }


    private Grammar.Atomic parseF() {
        String value = tkz.peek();
        Matcher matcher = Pattern.compile("^\\d+|\\w+$").matcher(value);
        if (matcher.find()) {
            return ArithExprFactory.getInstance().newArithmetic(tkz.consume());
        } else {
            tkz.consume("(");
            Grammar.Atomic atomic = ArithExprFactory.getInstance().newArithmetic(parseE());
            tkz.consume(")");
            return atomic;
        }
    }

    public AST parseAST() {
        return parseE();
    }
}
