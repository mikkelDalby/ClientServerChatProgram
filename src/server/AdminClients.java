package server;

import java.util.List;

// Every second count 1+ on each client. If 120 seconds reached remove client from list.
public class AdminClients extends Thread {
    private List<Client> clients;

    public AdminClients(List<Client> clients){
        this.clients = clients;
    }

    public void run(){
        boolean running = true;
        while (running){
            for (int i = clients.size()-1; i >= 0; i--) {
                if (clients.get(i).getAliveTime() >= 120){
                    System.out.println("removing user: " + clients.get(i).getUsername());
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