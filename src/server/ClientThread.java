package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread extends Thread {
    private Socket client;
    private Scanner input;
    private PrintWriter output;

    public ClientThread(Socket socket){
        //Set up reference to associated socket
        client = socket;

        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public void run(){
        String recieved;

        do {
            //Accept message from client on the socket's input stream
            recieved = input.nextLine();

            //Echo message back to client on the socket's output stream
            System.out.println(client.getPort() + " : " + recieved);
            output.println("ECHO: " + recieved);

            //Repeat above until 'QUIT' sent by client
        } while (!recieved.equals("QUIT"));

        try {
            if (client != null){
                System.out.println("Closing down connection");
                client.close();
            }
        } catch (IOException ioEx) {
            System.out.println("Unable to disconnect");
        }
    }
}
