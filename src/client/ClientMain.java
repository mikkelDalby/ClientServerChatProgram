package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientMain {
    private static InetAddress host;
    private static int port;

    public static boolean isConnected = false;

    //Set up stream for keyboard entry
    private static Scanner userEntry = new Scanner(System.in);

    private static ClientListener listener;
    private static ImAlive imAlive;

    public static void main(String[] args) {
        connectToServer();
    }

    private static void connectToServer(){
        Socket socket = null;
        Scanner networkInput;
        PrintWriter networkOutput;
        do {
                System.out.println("JOIN <<username>>, <<serverIp>>:<<serverPort>> \nConnect to server: ");
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
                    System.out.println("Connected to server");
                } else {
                    String[] splittedResponse = splitInputString(response);
                    System.out.print("Error: ");
                    for (int i = 1; i < splittedResponse.length; i++) {
                        System.out.print(splittedResponse[i] + " ");
                    }
                    System.out.println();
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
            PrintWriter networkOutput = new PrintWriter(socket.getOutputStream(), true);

            String message;
            listener = new ClientListener(socket);
            listener.start();

            imAlive = new ImAlive(socket);
            imAlive.start();

            while (isConnected){
                message = userEntry.nextLine();

                networkOutput.println(message);

                Thread.sleep(1);
            }
        } catch (IOException ioEx){
            ioEx.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("\nClosing connection");
                socket.close();
            } catch (IOException ioEx){
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
        connectToServer();
    }
}