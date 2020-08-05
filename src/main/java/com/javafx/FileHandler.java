package com.javafx;

import java.io.*;
import java.net.Socket;

public class FileHandler implements Runnable {

    private String serverFilesPath = "./src/main/resources/serverFiles";
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isRunning = true;
    private static int cnt = 1;


    public FileHandler(Socket socket) throws Exception {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        String userName = "user " + cnt;
        cnt++;
        serverFilesPath += "/" + userName;
        File dir = new File(serverFilesPath);
                if(!dir.exists()) {
                    dir.mkdir();
                }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                String command = in.readUTF();
                if (command.equals("./download")) {
                    String fileName = in.readUTF();
                    System.out.println("Найден файл с именем " + fileName);
                    File file = new File(serverFilesPath + "/" + fileName);
                    if (file.exists()) {
                        out.writeUTF("OK");
                        long len = file.length();
                        out.writeLong(len);

                    } else {
                        out.writeUTF("Такого файла не существует.");
                        FileInputStream fis = new FileInputStream(file);
                        byte [] buffer = new byte[1024];
                        while (fis.available() > 0) {
                            int count = fis .read(buffer);
                            out.write(buffer, 0, count);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
