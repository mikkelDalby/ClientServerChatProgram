package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientListener extends Thread {
    private Socket socket;

    public ClientListener(Socket socket){
        this.socket = socket;
    }

    public void run() {
        Scanner networkInput;
        try {
            networkInput = new Scanner(socket.getInputStream());
            boolean isOnline = true;
            do {
                String response;
                response = networkInput.nextLine();
                String[] splittedResponse = splitInputString(response);

                if (splittedResponse.length > 0) {
                    switch (splittedResponse[0]) {
                        case "LIST":
                            if (splittedResponse.length == 1) {
                                System.out.println("\nNo active users");
                            } else {
                                System.out.println("\nAll active users");
                                for (int i = 1; i < splittedResponse.length; i++) {
                                    System.out.println(splittedResponse[i]);
                                }
                            }
                                System.out.println();
                                System.out.println("Enter message: ");
                            break;
                        case "DATA":
                            System.out.println();
                            System.out.print(splittedResponse[1] + ": ");
                            for (int i = 2; i < splittedResponse.length; i++) {
                                System.out.print(splittedResponse[i] + " ");
                            }
                            System.out.println();
                            System.out.println("Enter message: ");
                            break;
                        case "J_ER":
                            System.out.print("Error: ");
                            for (int i = 1; i < splittedResponse.length; i++) {
                                System.out.print(splittedResponse[i] + " ");
                            }
                            System.out.println();
                            break;
                        case "HELP":
                            System.out.println();
                            String[] split = response.split(";");
                            for (int i = 0; i < split.length; i++) {
                                System.out.println(split[i]);
                            }
                            System.out.println();
                            System.out.println("Enter message: ");
                            break;
                        case "QUIT":
                            isOnline = false;
                            ClientMain.isConnected = false;
                            break;
                        default:

                            System.out.println("Response not recognized");
                    }
                }
                Thread.sleep(1);
            } while (isOnline);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String[] splitInputString(String input){
        try {
            String[] parameters = input.split(" |:");
            parameters[1] = parameters[1].replace(",", "");
            return parameters;
        } catch (ArrayIndexOutOfBoundsException e){
            String[] array = new String[1];
            array[0] = input;
            return array;
        }
    }

}
