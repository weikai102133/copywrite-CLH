package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TCPServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        len = is.read(bytes);
        System.out.println(new String(bytes,0,len));
        OutputStream os = socket.getOutputStream();
        int num = 0;
        while (num < 100) {
            os.write(("收到"+new Date().getTime()) .getBytes());
            TimeUnit.MILLISECONDS.sleep(100);
            num++;
        }
        socket.close();
        serverSocket.close();
    }
}
