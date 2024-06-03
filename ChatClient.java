// import packages
import java.io.*;
import java.net.*;
import java.util.Scanner;

// ChatClient class to start the chat client
public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    
    // Main method to start the chat client
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // Create a new thread to read messages from the server
            new Thread(new ReadMessage(socket)).start();

            // Create a PrintWriter to send messages to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Create a Scanner to read user input from the console
            Scanner scanner = new Scanner(System.in);

            // Continuously read user input and send it to the server
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle reading messages from the server
    private static class ReadMessage implements Runnable {
        private Socket socket;
        // Constructor to initialize the socket
        public ReadMessage(Socket socket) {
            this.socket = socket;
        }
        // Run method to read messages from the server
        @Override
        public void run() {
            try {
                // Create a BufferedReader to read messages from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;

                // Continuously read messages from the server and print them to the console
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
