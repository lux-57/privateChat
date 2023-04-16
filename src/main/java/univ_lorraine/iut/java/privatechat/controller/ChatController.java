package univ_lorraine.iut.java.privatechat.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;

public class ChatController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton;

    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;

    public void init(Socket socket) throws IOException {
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        Thread receiveThread = new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = input.readLine()) != null) {
                    final String message = "Serveur : " + serverResponse;
                    Platform.runLater(() -> chatArea.appendText(message + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        receiveThread.setDaemon(true);
        receiveThread.start();
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.trim().isEmpty()) {
            System.out.println(message);
            chatArea.appendText("Vous : " + message + "\n");
            messageField.clear();
        }
    }

    @FXML
    private void logout() throws IOException {
        System.out.println("quit");
        App.setRoot("login");
        closeResources();
    }

    private void closeResources() {
        try {
            if (input != null) {
                input.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
