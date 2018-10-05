package server;

import java.util.List;

// Every second count 1+ on each client. If 120 seconds reached remove client from list.
public class AdminClients extends Thread {
    private List<Client> clients;
    private boolean running = true;

    public AdminClients(List<Client> clients){
        this.clients = clients;
    }

    public void run(){
        while (running){

            for (int i = clients.size()-1; i > 0; i--) {
                if (clients.get(i).getAliveTime() >= 120){
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