# Java Networking Chat Application

This repository contains the Java source code for a simple networking chat application using sockets. The application is divided into two main components: `ChatServer.java` and `ChatClient.java`.

## ChatServer.java

The `ChatServer` class is responsible for handling multiple client connections and facilitating communication between them. It uses a `HashSet` to manage client handlers and runs on a predefined port. The server listens for incoming client connections, creates a new `ClientHandler` for each client, and starts a new thread for client communication.

Key features include:
- **Broadcasting messages** to all connected clients (excluding the sender)
- **Handling client disconnections** and removing client handlers

## ChatClient.java

The `ChatClient` class allows users to connect to the chat server and send/receive messages. It establishes a connection to the server, starts a new thread to read messages, and sends user input to the server.

Key features include:
- **Reading user input** from the console and sending it to the server
- **Receiving messages** from the server and displaying them to the user

## Usage

1. Start the chat server by running `ChatServer.java`.
2. Connect to the chat server by running `ChatClient.java`.
3. Enter messages in the console to communicate with other connected clients.

