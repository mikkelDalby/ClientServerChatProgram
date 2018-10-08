package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Every second count 1+ on each client. If 120 seconds reached remove client from list.
public class AdminClients extends Thread {
    private List<Client> clients;
    private PrintWriter output;

    public AdminClients(List<Client> clients){
        this.clients = clients;
    }

    public void run(){
        boolean running = true;
        while (running){
            for (int i = clients.size()-1; i >= 0; i--) {
                if (clients.get(i).getAliveTime() >= 120){
                    System.out.println("Removing user: " + clients.get(i).getUsername());
                    try {
                        output = new PrintWriter(clients.get(i).getClientSocket().getOutputStream(), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    clients.get(i).killClient();
                    clients.get(i).interrupt();
                    clients.remove(i);
                } else {
                    clients.get(i).addSecondToAliveTime();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}