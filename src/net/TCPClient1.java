package net;

import sun.reflect.generics.scope.Scope;

import java.io.*;
import java.net.Socket;

public class TCPClient1 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8888);
        FileInputStream fis = new FileInputStream("F:\\FileInputStream.txt");
        OutputStream os  = socket.getOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fis.read(bytes)) > 0){
            os.write(bytes,0,len);
        }
        socket.shutdownOutput();
        InputStream is = socket.getInputStream();
        while ((len = is.read(bytes)) > 0){
            System.out.println(new String(bytes,0,len));
        }
        socket.close();
    }
}
