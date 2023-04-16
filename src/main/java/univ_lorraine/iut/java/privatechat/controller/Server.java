package univ_lorraine.iut.java.privatechat.controller;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("Serveur en attente de connexion...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connecté : " + socket.getInetAddress());

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        Thread receiveThread = new Thread(() -> {
            String message;
            try {
                while ((message = input.readLine()) != null) {
                    System.out.println("Message reçu: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread sendThread = new Thread(() -> {
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            try {
                while (true) {
                    System.out.print("Entrez un message (tapez 'quit' pour quitter) : ");
                    userInput = keyboard.readLine();
                    output.println(userInput);
                    if (userInput.equalsIgnoreCase("quit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        receiveThread.start();
        sendThread.start();

        try {
            receiveThread.join();
            sendThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Fermeture de la connexion.");
        input.close();
        output.close();
        socket.close();
        serverSocket.close();
    }
}


