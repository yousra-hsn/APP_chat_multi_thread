# Chat-multi-thread

## But du projet 

Le projet consiste à développer une application de chat en réseau utilisant des sockets en Java. L’objectif principal est de créer un système de communication entre plusieurs clients connectés à un même serveur. Chaque client peut envoyer et recevoir des messages en temps réel, facilitant ainsi les échanges simultanés. Ce type de projet s’inspire des besoins croissants de communication instantanée dans des environnements distribués, tels que les applications de messagerie, les forums de discussion en ligne etc..

## Concepts clés

- Architecture Client-Serveur : Le projet repose sur l’architecture réseau classique client-serveur. Le serveur est un programme central qui écoute les connexions des clients, gère la communication entre eux et assure le bon fonctionnement du système. Les clients sont des programmes qui se connectent au serveur pour échanger des messages.

- Sockets : Les sockets sont des mécanismes permettant la communication bidirectionnelle entre deux entités (le client et le serveur, dans ce cas) sur un réseau. Le serveur utilise un socket pour accepter des connexions entrantes, tandis que chaque client en utilise un pour se connecter au serveur.

- Multi-threading : Le serveur est capable de gérer plusieurs connexions simultanées en allouant un thread à chaque client. Cela permet à plusieurs clients d’interagir sans attendre que le serveur soit libre pour traiter leurs requêtes.

## Contribution

Projet académique réalisé dans le cadre de l'UV AI13 à l'Université de Technologie de Compiègne. **Octobre 2024**

3 membres : 
- **Jana Rafei**
- **Ikram Dama**
- **Yousra Hassan**