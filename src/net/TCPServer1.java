package net;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer1 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    InputStream is = socket.getInputStream();
                    File file = new File("D:\\upload");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream(file + "\\FileInputStream.txt");
                    OutputStream os = socket.getOutputStream();
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = is.read(bytes)) > 0) {
                        fos.write(bytes, 0, len);
                    }
                    os.write("上传成功".getBytes());

                    fos.close();
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {

                }
            }).start();


        }
        //serverSocket.close();
    }
}
