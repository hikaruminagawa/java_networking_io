import java.io.*;  
import java.net.*;  
import java.util.Scanner;  
  
public class ChatClient {  
    private static final String SERVER_ADDRESS = "localhost";  
    private static final int SERVER_PORT = 12345;  
  
    public static void main(String[] args) {  
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {  
            new Thread(new ReadMessage(socket)).start();  
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  
            Scanner scanner = new Scanner(System.in);  
            while (true) {  
                String message = scanner.nextLine();  
                out.println(message);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private static class ReadMessage implements Runnable {  
        private Socket socket;  
  
        public ReadMessage(Socket socket) {  
            this.socket = socket;  
        }  
  
        @Override  
        public void run() {  
            try {  
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                String message;  
                while ((message = in.readLine()) != null) {  
                    System.out.println(message);  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}  
