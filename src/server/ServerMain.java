package server;

import java.io.*;
import java.net.*;
import java.util.*;

// Waits for new clients to join the server and adds them to the activeClients list
public class ServerMain {
    private static ServerSocket serverSocket;
    private static final int port = 1234;

    public static List<Client> activeClients = new ArrayList<>();

    private static Scanner input;
    private static PrintWriter output;

    private static AdminClients adminClients = new AdminClients(activeClients);
    private static SendUsernamesThread sendUsernames;

    public static void main(String[] args) throws IOException{
        try{
            serverSocket = new ServerSocket(port);
            sendUsernames = new SendUsernamesThread(activeClients);
            sendUsernames.start();
        } catch (IOException ioEx){
            System.out.println("\nUnable to setup port!");
            System.exit(1);
        }

        // Start thread for checking if user is inactive
        adminClients.start();

        while (true){
            // Wait for clientSocket..
            Socket clientSocket = serverSocket.accept();
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            String[] clientRequest = splitInputString(input.nextLine());

            if (clientRequest.length == 4 && clientRequest[0].equals("JOIN")){
                boolean isUsed = false;

                // Check if username is already taken
                for (Client i: activeClients){
                    if (i.getUsername().equals(clientRequest[1])){
                        isUsed = true;
                    }
                }

                // If username is free
                if (!isUsed){
                    // Create a thread to handle communication with this client and pass the constructor
                    // for this thread reference to the relevant socket..
                    Client client = new Client(clientSocket, clientRequest[1], activeClients);
                    activeClients.add(client);
                    // Send is connected message
                    output.println(generateOkResponse());
                    System.out.println("New client accepted: " + client.getUsername());
                    client.start();

                } else {
                    output.println(generateErrorMessage(2, "Username already in use"));
                }

            } else {
                // Client request not starting with JOIN
                output.println(generateErrorMessage(1, "Use keyword JOIN to join this server"));
            }
        }
    }

    public static String[] splitInputString(String input){
        try {
            String[] parameters = input.split(" |:");
            parameters[1] = parameters[1].replace(",", "");
            return parameters;
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Client sent wrong JOIN string");
            String[] array = new String[1];
            array[0] = "notSet";
            return array;
        }
    }

    public static String generateOkResponse(){
        return "J_OK";
    }

    public static String generateErrorMessage(int errorCode, String errorMessage){
        return "J_ER " + errorCode + ": " + errorMessage;
    }
}