package univ_lorraine.iut.java.privatechat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {
    private static final String PASSWORDS_DIR = "passwords";
    private static final String PASSWORD_HASH_ALGORITHM = "SHA-256";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public LoginController() {
        // Crée le répertoire des mots de passe s'il n'existe pas
        File passwordsDirectory = new File(PASSWORDS_DIR);
        if (!passwordsDirectory.exists()) {
            passwordsDirectory.mkdir();
        }
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (checkCredentials(username, password)) {
                App.setRoot("chat");
            } else {
                System.out.println("Pseudo ou mot de passe incorrect.");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCredentials(String username, String password) throws IOException, NoSuchAlgorithmException {
        File userFile = new File(PASSWORDS_DIR + File.separator + username + ".pwd");
        if (userFile.exists()) {
            String storedHash = Files.readString(userFile.toPath());
            String passwordHash = hashPassword(password);
            return storedHash.equals(passwordHash);
        } else {
            createUser(username, password);
            return true;
        }
    }

    private void createUser(String username, String password) throws IOException, NoSuchAlgorithmException {
        String passwordHash = hashPassword(password);
        File userFile = new File(PASSWORDS_DIR + File.separator + username + ".pwd");
        try (FileWriter writer = new FileWriter(userFile)) {
            writer.write(passwordHash);
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM);
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
