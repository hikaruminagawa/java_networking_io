import java.io.*;  
import java.net.*;  
import java.util.*;  
  
public class ChatServer {  
    private static final int PORT = 12345;  
    private static Set<ClientHandler> clientHandlers = new HashSet<>();  
    private static int userId = 0;  
  
    public static void main(String[] args) {  
        System.out.println("Chat server started...");  
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {  
            while (true) {  
                Socket clientSocket = serverSocket.accept();  
                ClientHandler clientHandler = new ClientHandler(clientSocket, userId++);  
                clientHandlers.add(clientHandler);  
                new Thread(clientHandler).start();  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static synchronized void broadcastMessage(String message, ClientHandler sender) {  
        for (ClientHandler clientHandler : clientHandlers) {  
            if (clientHandler != sender) {  
                clientHandler.sendMessage(message);  
            }  
        }  
    }  
  
    public static synchronized void removeClient(ClientHandler clientHandler) {  
        clientHandlers.remove(clientHandler);  
    }  
  
    private static class ClientHandler implements Runnable {  
        private Socket socket;  
        private PrintWriter out;  
        private int userId;  
  
        public ClientHandler(Socket socket, int userId) {  
            this.socket = socket;  
            this.userId = userId;  
        }  
  
        @Override  
        public void run() {  
            try {  
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                out = new PrintWriter(socket.getOutputStream(), true);  
                String message;  
                while ((message = in.readLine()) != null) {  
                    System.out.println("User " + userId + ": " + message);  
                    ChatServer.broadcastMessage("User " + userId + ": " + message, this);  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    socket.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                ChatServer.removeClient(this);  
                System.out.println("User " + userId + " disconnected.");  
            }  
        }  
  
        public void sendMessage(String message) {  
            out.println(message);  
        }  
    }  
}  
