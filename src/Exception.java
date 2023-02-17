public class Exception {
    public static class SyntaxError extends RuntimeException {
        public SyntaxError(String message) {
            super(message);
        }
    }
    public static class ArithmeticError extends RuntimeException {
        public  ArithmeticError(String message) { super(message); }
    }
    public static class TokenError extends RuntimeException {
        public TokenError(String message) {
            super(message);
        }
    }
    public static class OperatorNotFound extends RuntimeException {
        public OperatorNotFound(String message) {
            super(message);
        }
    }



}
