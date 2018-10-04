package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain {
    private static ServerSocket serverSocket;
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException{
        try{
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ioEx){
            System.out.println("\nUnable to setup port!");
            System.exit(1);
        }

        do {
            // Wait for client..
            Socket client = serverSocket.accept();

            System.out.println("\nNew client accepted. \n");

            // Create a thread to handle communication with this client and pass the constructor
            // for this thread referance to the relevant socket..
            ClientThread handler = new ClientThread(client);
            handler.start();
        } while (true);
    }
}