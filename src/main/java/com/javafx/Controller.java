package com.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView<String> lv;
    public Button Send;
    public TextField txt;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final String clientFilesPath = "./src/main/resources/clientFiles";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        File dir = new File(clientFilesPath);
        for (String file : dir.list()) {
            lv.getItems().add(file);
        }

    }

    public void sendCommand(ActionEvent actionEvent) {
        String command = txt.getText();
        String [] op = command.split("");
        if (op[0].equals("./download")) {
            try {
                out.writeUTF(op[0]);
                out.writeUTF(op[1]);
                String response = in.readUTF();
                System.out.println("resp: response");
                if (response.equals("OK")) {
                    File file = new File(clientFilesPath + "/" + op[1]);
                    if(!file.exists()) {
                        file.createNewFile();
                    }
                    long len = in.readLong();
                    byte [] buffer = new byte[1024];
                    try(FileOutputStream fos = new FileOutputStream(file)) {

                    if (len < 1024) {

                        int count = in.read(buffer);
                        fos.write(buffer, 0, count);

                        } else {
                                for (long i = 0; i < len / 1024; i++) {
                                    int count = in.read(buffer);
                                    fos.write(buffer, 0, count);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {

        }
    }
}
