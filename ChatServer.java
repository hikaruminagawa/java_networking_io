// import packages
import java.io.*;
import java.net.*;
import java.util.*;

// ChatServer class to start the chat server
public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static int userId = 0;

   // Main method to start the chat server 
    public static void main(String[] args) {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                // Create a new ClientHandler for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, userId++);
                // Add the ClientHandler to the set of active client handlers
                clientHandlers.add(clientHandler);
                // Start a new thread to handle client communication
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast a message to all connected clients
    public static synchronized void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }

    // Remove a client from the set of active client handlers
    public static synchronized void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }
    // ClientHandler class to handle client communication
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private int userId;
        // Constructor to initialize the client handler
        public ClientHandler(Socket socket, int userId) {
            this.socket = socket;
            this.userId = userId;
        }
        // Run method to handle client communication
        @Override
        public void run() {
            try {
                // Create input and output streams for client communication
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String message;
                // Read messages from the client and broadcast them to other clients
                while ((message = in.readLine()) != null) {
                    System.out.println("User " + userId + ": " + message);
                    ChatServer.broadcastMessage("User " + userId + ": " + message, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Close the client socket
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Remove the client handler from the set of active client handlers
                ChatServer.removeClient(this);
                System.out.println("User " + userId + " disconnected.");
            }
        }

        // Send a message to the client
        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
