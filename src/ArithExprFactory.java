public class ArithExprFactory {
    private ArithExprFactory() {
    }

    private static ArithExprFactory factory;

    public static ArithExprFactory getInstance() {
        if (factory == null)
            factory = new ArithExprFactory();
        return factory;
    }

    public Grammar.BinaryArithmetic newArithmetic(AST lhs, String op, AST rhs) {
        return new Grammar.BinaryArithmetic(lhs, op, rhs);
    }

    public Grammar.Atomic newArithmetic(AST ast) {
        return new Grammar.Atomic(ast);
    }

    public Grammar.Atomic newArithmetic(String n) {
        return new Grammar.Atomic(n);
    }
}
