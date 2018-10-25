package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client extends Thread {
    private volatile Socket client;
    private volatile String username;
    private volatile int aliveTime = 0;

    private List<Client> activeClients;

    private Scanner input;
    private PrintWriter output;

    private boolean isAlive = true;

    public Client(Socket socket, String username, List<Client> activeClients){
        this.client = socket;
        this.username = username;
        this.activeClients = activeClients;

        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public void run(){
        while (isAlive) {
            String response = input.nextLine();
            String[] splittedResponse = splitInputString(response);
            switch (splittedResponse[0]){
                case "IMAV":
                    resetAliveTime();
                    break;
                case "DATA":
                    for (Client i: activeClients){
                        if (i.getClientSocket() != getClientSocket()) {
                            try {
                                PrintWriter send = new PrintWriter(i.getClientSocket().getOutputStream(), true);
                                String string = "DATA " + getUsername() + ": ";
                                for (int j = 2; j < splittedResponse.length; j++) {
                                    string += splittedResponse[j] + " ";
                                }
                                send.println(string);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case "MSG":
                    boolean userFound = false;
                    for (Client i: activeClients){
                        if (i.getUsername().equals(splittedResponse[1])){
                            try {
                                PrintWriter send = new PrintWriter(i.getClientSocket().getOutputStream(), true);
                                String string = "DATA " + getUsername() + ": ";
                                for (int j = 2; j < splittedResponse.length; j++) {
                                    string += splittedResponse[j] + " ";
                                }
                                send.println(string);
                                userFound = true;
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (!userFound){
                        try {
                            PrintWriter send = new PrintWriter(getClientSocket().getOutputStream(), true);
                            String string = "J_ER 4: User with username: " + splittedResponse[1] + " not found.";
                            send.println(string);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "HELP":
                    String line = "HELP ;DATA <<your_username>>:<<message>> - Sends message to all active users " +
                            ";MSG <<username>>: <<message>> - Sends message to user with provided username" +
                            ";QUIT - Close connection to server";
                    output.println(line);
                    break;
                case "QUIT":
                    output.println("QUIT");
                    for (Client i: activeClients){
                        if (i.getUsername().equals(getUsername())){
                            System.out.println("Client has quit: " + i.getUsername());
                            activeClients.remove(i);
                            break;
                        }
                    }
                    killClient();
                    break;
                default:
                    output.println("J_ER 3: Input not recognized");
            }
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


    public synchronized void resetAliveTime(){
        this.aliveTime = 0;
    }

    public synchronized void addSecondToAliveTime(){
        this.aliveTime++;
    }

    public synchronized int getAliveTime(){
        return aliveTime;
    }

    public synchronized String getUsername(){
        return username;
    }

    public synchronized Socket getClientSocket(){
        return client;
    }

    public synchronized void killClient(){
        isAlive = false;
    }
}