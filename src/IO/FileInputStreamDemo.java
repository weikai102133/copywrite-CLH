package IO;

import java.io.FileInputStream;
import java.io.IOException;

public class FileInputStreamDemo {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = null;
        try{
            fis = new FileInputStream("F:\\FileInputStream.txt");
            byte[] b = new byte[1024];
            int hasRead = 0;
            while((hasRead = fis.read(b)) > 0){
                System.out.println(new String(b,0,hasRead));
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            fis.close();
        }
    }


}
