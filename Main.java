import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
       Parser parser = new Parser(new Frame(readData(new File("./src/test.txt"))));
       parser.prinfFrame();
    }

    private static String readData(File file) {
        BufferedReader bf;
        StringBuilder sb = new StringBuilder();
        try {
            bf = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bf.readLine())!=null){
                sb.append(line);
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
