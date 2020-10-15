package IO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStreamDemo {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream("F:\\FileInputStream.txt");
            fos = new FileOutputStream("F:\\FileOutputStream.txt");
            byte[] b = new byte[1024];
            int hasRead = 0;
            while((hasRead = fis.read(b)) > 0){
                fos.write(b,0,hasRead);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            fis.close();
            fos.close();
        }
    }
}
