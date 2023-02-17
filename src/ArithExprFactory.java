public class ArithExprFactory implements AST{

}

public class ASTFactory {
    public AST newAST(){
        return new ArithExprFactory();
    }
}
