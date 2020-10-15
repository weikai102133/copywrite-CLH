package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8888);
        OutputStream os = socket.getOutputStream();
        os.write("这是8888端口".getBytes());
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) > 0){
            System.out.println(new String(bytes,0,len));
        }
        socket.close();
    }
}
