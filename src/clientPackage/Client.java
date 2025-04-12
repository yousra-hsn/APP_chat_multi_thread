package clientPackage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    private Socket socket;
    private BufferedReader input; // pour écouter les messages de serveur
    private PrintWriter output; // pour envoyer des messages au serveur


    public static void main(String[] args) {
        new Client().start();
    }



    private void start() {
        try {
            // Connexion au serveur
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // Initialisation des flux
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //ecouter le serveur
            output =new PrintWriter(socket.getOutputStream(), true); // pour envoyer un message au serveur

            // Lire la demande de nom du client
            System.out.println(input.readLine());

            // Saisie du pseudonome
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            //envoie de nom au serveur
            output.println(username);

            // Lancer le thread pour écouter les messages du serveur
            new ThreadListener().start();

            // Lancer le thread pour envoyer les messages
            new UserInputHandler().start();

        } catch (IOException e) {
            System.out.println("Erreur de connexion au serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Thread pour écouter les messages du serveur
    private class ThreadListener extends Thread {
        @Override
        public void run() {
            try {
                String message;
                while (( (message = input.readLine()) != null)) {
                    System.out.println(message);
                }
            } catch (SocketException e) { //  Lorsqu'une exception survient (par exemple, lorsque le serveur est déconnecté), il informe l'utilisateur que le serveur est hors ligne.
                System.out.println("Serveur deconnecté ");
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Erreur lors de la réception des messages du serveur : " + e.getMessage());
            } finally {
                closeEverything();  // S'assurer que les ressources sont fermées
            }
        }

    }

    // Thread pour gérer les messages saisis par l'utilisateur
    private class UserInputHandler extends Thread {
        @Override
        public void run() {

            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();

            while ( userInput != null) {
                output.println(userInput);
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                userInput = scanner.nextLine();
            }
            closeEverything();

        }
    }

    // Méthode pour fermer proprement le socket et les flux
    private synchronized void closeEverything() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
        }
    }

}

