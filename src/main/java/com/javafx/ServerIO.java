package com.javafx;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerIO {

    private boolean isRunning = true;



    public void stop() {
        isRunning = false;
    }

    public ServerIO() {
        try(ServerSocket server = new ServerSocket(8189)) {
            System.out.println("Сервер стартовал.");

            while (isRunning) {
                Socket conn = server.accept();
                System.out.println("Клиент присоеденился.");
                new Thread(new FileHandler(conn)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerIO();
    }
}
