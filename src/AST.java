import java.util.Map;

public interface AST {

    void prettyPrint(StringBuilder builder);
    int eval(Map<String, Integer> bindings);
}

