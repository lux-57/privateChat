package univ_lorraine.iut.java.privatechat.controller;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws IOException {

        String serverAddress = "100.64.48.79";
        int port = 12345;

        Socket socket = new Socket(serverAddress, port);
        System.out.println("La connexion est établie avec le serveur: \n");

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);


        Thread receiveThread = new Thread(() -> {
            String serverResultat;
            try {
                while ((serverResultat = input.readLine()) != null) {
                    System.out.println("\n Message reçu de la part du serveur : " + serverResultat);
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
                    System.out.print("Entrez votre message (tapez 'q' pour quitter) : ");
                    userInput = keyboard.readLine();
                    output.println(userInput);
                    if (userInput.equalsIgnoreCase("q")) {
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
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("La session est close");
        input.close();
        output.close();
        socket.close();
    }
}
