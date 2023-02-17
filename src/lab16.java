import java.io.*;
import java.util.Scanner;

class Reader {
    private final BufferedReader bufferReader;
    private int current = 1;

    public Reader(String filename) throws FileNotFoundException {
        FileReader fileReader = new FileReader(filename);
        this.bufferReader = new BufferedReader(fileReader);
    }

    public String getLine() {
        try {
            current++;
            return this.bufferReader.readLine();
        } catch (IOException e) {
            current--;
            System.out.println("Read file error at line " + current);
            return null;
        }
    }

    public int getLineNumber() {
        return current;
    }
}

class Writer {
    private final FileWriter fileWriter;

    public Writer(String filename) throws IOException {
        this.fileWriter = new FileWriter(filename);
    }

    public void writeEquation(String equation) throws IOException {
        fileWriter.append(String.valueOf(equation));
    }

    public void writeResult(int result) throws IOException {
        fileWriter.append(String.valueOf(result)).append("\n");
    }

    public void writeError(IOException error) {
        try {
            fileWriter.append(error.getMessage()).append("\n");
        } catch (Exception.SyntaxError e) {
            System.out.println("Error during writing exception!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        fileWriter.close();
    }
}

public class lab16 {
    private static Reader getReader(Scanner scanner, String file) {
        Reader reader;
        try {
            reader = new Reader(file);
        } catch (IOException e) {
            System.out.println("invalid problem file! please enter new filename");
            reader = getReader(scanner, scanner.nextLine());
        }
        return reader;
    }

    private static Writer getWriter(Scanner scanner, String file) {
        Writer writer;
        try {
            writer = new Writer(file);
        } catch (IOException e) {
            System.out.println("invalid result file! please enter new filename");
            writer = getWriter(scanner, scanner.nextLine());
        }
        return writer;
    }

    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);
            String problemFile = "problems.txt", resultFile = "results.txt", line;
            Reader reader = getReader(scanner, problemFile);
            Writer writer = getWriter(scanner, resultFile);
            line = reader.getLine();
            while (line != null) {
                try {
                    Tokenizer tkz = new Tokenizer(line);
                    Parser psr = new Parser(tkz);
                    StringBuilder builder = new StringBuilder();
                    AST ast = psr.parseAST();
                    ast.prettyPrint(builder);
                    int result = ast.eval(null);
                    writer.writeEquation(builder.toString() + " = ");
                    writer.writeResult(result);
                } catch (IOException e) {
                    writer.writeError(e);
                    System.out.println(e.getMessage() + " at line " + (reader.getLineNumber() - 1));
                } catch (Exception.SyntaxError | Exception.ArithmeticError | Exception.TokenError error) {
                    System.out.println(error.getMessage() + " at line " + (reader.getLineNumber() - 1));
                }
                line = reader.getLine();
            }
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("problem during closing file!");
            }
        }

}
