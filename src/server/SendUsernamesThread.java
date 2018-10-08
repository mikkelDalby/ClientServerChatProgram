package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static server.ServerMain.activeClients;

public class SendUsernamesThread extends Thread {

    private PrintWriter output;
    private List<Client> activeClients;
    private int tempSize = 0;

    public SendUsernamesThread(List<Client> activeClients){
        this.activeClients = activeClients;
    }

    public void run() {
        while (true){
            if (activeClients.size() != tempSize) {
                tempSize = activeClients.size();
                broadcastListOfActiveUsers();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendList(Client client) {
        try {
            output = new PrintWriter(client.getClientSocket().getOutputStream(), true);
            String list = "LIST ";
            for (Client i: activeClients){
                if (!i.getUsername().equals(client.getUsername())) {
                    list += i.getUsername() + " ";
                }
            }
            String[] check = list.split(" ");
            if (check.length > 1) {
                output.println(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastListOfActiveUsers(){
        System.out.println("Updating active users");
        for (Client i: activeClients){
            sendList(i);
        }
    }
}