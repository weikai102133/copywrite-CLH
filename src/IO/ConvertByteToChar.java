package IO;

import java.io.*;

public class ConvertByteToChar {
    public static void main(String[] args) throws IOException {
        InputStream is =null;
        Reader reader = null;
        try {
            File file = new File("F:\\FileInputStream.txt");
            is = new FileInputStream(file);
            reader = new InputStreamReader(is,"gbk");
            char[] byteArray = new char[(int) file.length()];
            int size = reader.read(byteArray);
            System.out.println("大小:" + size + ";内容:" + new String(byteArray));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            reader.close();
            is.close();
        }
    }
}
