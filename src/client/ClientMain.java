package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientMain {
    private static InetAddress host;
    private static int port;

    private static boolean isConnected = false;

    //Set up stream for keyboard entry
    private static Scanner userEntry = new Scanner(System.in);

    public static void main(String[] args) {
        connectToServer();
    }

    private static void connectToServer(){
        Socket socket = null;
        Scanner networkInput;
        PrintWriter networkOutput;
        do {
                System.out.println("Connect to server: JOIN <<username>>, <<serverIp>>:<<serverPort>>");
                String message = userEntry.nextLine();
                String[] parameters = splitInputString(message);

            try {
                host = InetAddress.getByName(parameters[2]);
                socket = new Socket(host, Integer.parseInt(parameters[3]));
                networkInput = new Scanner(socket.getInputStream());
                networkOutput = new PrintWriter(socket.getOutputStream(), true);

                networkOutput.println(message);
                String response = networkInput.nextLine();

                if (response.equals("J_OK")){
                    isConnected = true;
                } else {
                    System.out.println("Server did'nt respond try again");
                }

            } catch (UnknownHostException uhEx) {
                System.out.println("\nHost not found!\n");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Message not valid");
            }
        } while (!isConnected);
            sendMessages(socket);
    }

    private static String[] splitInputString(String input){
        try {
            String[] parameters = input.split(" |:");
            parameters[1] = parameters[1].replace(",", "");
            return parameters;
        } catch (ArrayIndexOutOfBoundsException e){
            String[] array = new String[1];
            array[0] = "notSet";
            return array;
        }
    }

    private static void sendMessages(Socket socket){
        try {
            Scanner networkInput = new Scanner(socket.getInputStream());
            PrintWriter networkOutput = new PrintWriter(socket.getOutputStream(), true);

            String message, response;

            while (true){
                System.out.print("Enter message ('QUIT' to exit): ");
                message = userEntry.nextLine();

                // Send message to server on the socket's output stream
                //Accept response from server on the socket's input stream
                networkOutput.println(message);

                response = networkInput.nextLine();

                //Display servers response to user
                System.out.println("\nSERVER> " + response);
            }
        } catch (IOException ioEx){
            ioEx.printStackTrace();
        }

        finally {
            try {
                System.out.println("\nClosing connection");
                socket.close();
            } catch (IOException ioEx){
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
    }
}
