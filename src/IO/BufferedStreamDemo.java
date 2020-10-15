package IO;

import java.io.*;

public class BufferedStreamDemo {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            fis = new FileInputStream("F:\\FileInputStream.txt");
            fos = new FileOutputStream("F:\\FileOutputStream.txt");
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            byte[] b = new byte[1024];
            int hasRead = 0;
            while((hasRead = bis.read(b)) > 0){
                bos.write(b,0,hasRead);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            bis.close();
            bos.close();
        }
    }
}
