package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

// Classe interne pour gérer chaque client
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter out; //envoyer des messages au client
    private BufferedReader in; // lire les messages envoyés par le client
    private String username; //le pseudonyme d'utilisateur du client

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            // Crée des flux de communication avec le client
            out = new PrintWriter(clientSocket.getOutputStream(), true); // flux pour écrire au client
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // flux pour lire les messages envoyés par le client
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            // Demande le nom d'utilisateur
            out.println("Veuillez entrer votre pseudonyme: ");
            String name = in.readLine();

            verifyUsername(name);

            System.out.println(username + " est connecté sur le serveur.");
            // diffuser le message de nouveau client arrivé
            diffuserMessage(username + " a rejoint la conversation.", this, true);

            String message;
            while (true) {

                message = in.readLine(); // Lire les messages du client
                if ( message.equalsIgnoreCase("exit")) {
                    break; // Si "exit" ou la connexion est coupée
                }
                System.out.println(username + " a envoyé : " + message );
                diffuserMessage(this.username + " a dit : " + message, this, false);
            }
        } catch (SocketException e) {
            System.out.println("Déconnexion inattendue du client : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur de communication avec le client : " + e.getMessage());
        } finally {
            disconnectHandler(); // Fermer proprement la connexion
        }
    }

    private void verifyUsername(String name) throws IOException {
        synchronized (Serveur.clients) {
            for (ClientHandler client : Serveur.clients) {
                // Vérifie si le nom d'utilisateur est déjà utilisé
                if (client.username != null && client.username.equalsIgnoreCase(name)) {
                    // Si oui, demande un nouveau pseudonyme
                    out.println("Le pseudonyme '" + name + "' est déjà utilisé. Veuillez en choisir un autre : ");
                    name = in.readLine(); // Lire un nouveau nom d'utilisateur
                    verifyUsername(name); // Vérifier à nouveau le nouveau nom
                    return; // Sortir de la méthode si un nouveau nom est validé
                }
            }
            // Si le nom est unique, l'attribuer
            this.username = name;
        }
    }

    // Méthode pour envoyer un message au client
    private void envoyerMessage(String message, boolean newClient) {
        out.println(message);
        if (newClient) { // si la message est que un nouveau client arrive
            out.println("------------------------------------------------");
        }
    }

    /**
     * Gère la déconnexion du client et libère les ressources associées.
     */
    private void disconnectHandler() {

        synchronized (Serveur.clients) {
            Serveur.clients.remove(this);
        }

        try {
            if (in != null) {
                in.close();  // Fermer le flux d'entrée
            }
            if (out != null) {
                out.close();  // Fermer le flux de sortie
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close(); // Fermer la socket
            }

            diffuserMessage(username + " a quitté la conversation.", this, false);

        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture de la socket du client : " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println(  username+ " a quitté la conversation ");
    }

    // Méthode pour diffuser un message à tous les clients connectés
    private void diffuserMessage(String message, ClientHandler exclureClient, Boolean newClient) {
        for (ClientHandler client : Serveur.clients) {
            if (client != exclureClient) {
                client.envoyerMessage( message, newClient);
            }
        }
    }

}