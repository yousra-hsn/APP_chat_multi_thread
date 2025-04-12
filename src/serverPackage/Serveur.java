package serverPackage;
import java.io.*;
import java.net.*;
import java.util.*;

public class Serveur {

    private static final int PORT = 1234;

    // ensemble de clients
    //synchronizedSet : pour garantir qu'un seule thread le modifie à un moment donnée (les opérations sont effectué d'une manière sequentielle)
    protected static final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        ServerSocket serveurSocket = null;

        try {
            // Crée un serveur socket sur le port 1234
            serveurSocket = new ServerSocket(PORT);
            System.out.println("Le serveur est démarré sur le port "  + PORT);

            // Boucle infinie pour accepter les connexions
            while (true) {
                // Accepte une nouvelle connexion client
                Socket socket = serveurSocket.accept();

                // Créer un nouveau client handler pour gérer cette connexion
                ClientHandler clientHandler = new ClientHandler(socket);

                // Ajouter le client à la liste des clients
                synchronized (clients) {
                    clients.add(clientHandler);
                }
                clientHandler.start();  // Démarrer le thread du client
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (serveurSocket != null) {
                    serveurSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Erreur du serveur : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}
