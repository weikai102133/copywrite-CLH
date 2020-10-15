package IO;

import java.io.FileReader;
import java.io.IOException;

public class FileReaderDemo {
    public static void main(String[] args) throws IOException {
        FileReader fr = null;
        try{
            fr = new FileReader("F:\\FileReader.txt");
            char[] b = new char[1024];
            int hasRead = 0;
            while ((hasRead = fr.read(b)) > 0) {
                System.out.println(new String(b,0,hasRead));
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            fr.close();
        }
    }
}
