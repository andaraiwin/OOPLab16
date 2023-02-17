import java.util.Map;
import java.util.regex.Pattern;

public class Grammar {
    private static abstract class BinaryArithmetic implements AST {
        private AST lhs, rhs;
        private String op;

        public BinaryArithmetic(AST lhs, String op, AST rhs) {
            this.lhs = lhs;
            this.op = op;
            this.rhs = rhs;
        }

        @Override
        public void prettyPrint(StringBuilder builder) {
            builder.append('(');
            lhs.prettyPrint(builder);
            builder.append(op);
            rhs.prettyPrint(builder);
            builder.append(')');
        }
    }

    public static class T extends BinaryArithmetic {
        public T(AST lhs, String op, AST rhs) {
            super(lhs, op, rhs);
        }

        @Override
        public int eval(Map<String, Integer> bindings) {
            switch (super.op) {
                case "*":
                    return super.lhs.eval(bindings) * super.rhs.eval(bindings);
                case "/":
                    if (super.rhs.eval(bindings) == 0) throw new Exception.ArithmeticError("Can't be divided by zero");
                    return super.lhs.eval(bindings) / super.rhs.eval(bindings);
                case "%":
                    if (super.rhs.eval(bindings) == 0) throw new Exception.ArithmeticError("Can't be modulo by zero");
                    return super.lhs.eval(bindings) % super.rhs.eval(bindings);
                default:
                    throw new Exception.OperatorNotFound(super.op);
            }
        }
    }

    public static class E extends BinaryArithmetic {
        public E(AST lhs, String op, AST rhs) {
            super(lhs, op, rhs);
        }

        @Override
        public int eval(Map<String, Integer> bindings) {
            return switch (super.op) {
                case "+" -> super.lhs.eval(bindings) + super.rhs.eval(bindings);
                case "-" -> super.lhs.eval(bindings) - super.rhs.eval(bindings);
                default -> throw new Exception.OperatorNotFound(super.op);
            };
        }
    }

    public static class F implements AST {
        private String n;
        private AST ast;

        public F(AST ast) {
            this.ast = ast;
        }

        public F(String n) {
            this.n = n;
        }

        private boolean isNumber(String string) {
            if (string == null) return false;
            return Pattern.compile("^\\d+$").matcher(string).find();
        }

        @Override
        public void prettyPrint(StringBuilder builder) {
            if (ast != null) {
                ast.prettyPrint(builder);
            } else {
                if (isNumber(n)) builder.append(Integer.parseInt(n));
                else builder.append(n);
            }
        }

        @Override
        public int eval(Map<String, Integer> bindings) {
            if (ast != null) {
                return ast.eval(bindings);
            }
            if (isNumber(n)) return Integer.parseInt(n);
            else return bindings.get(n);
        }
    }
}
