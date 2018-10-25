package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Check if changes in the active cilents list. If changes occur broadcast list of usernames to all active users.
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
                if (activeClients.size() == 1) {
                    for (Client i: activeClients) {
                        try {
                            output = new PrintWriter(i.getClientSocket().getOutputStream(), true);
                            output.println("LIST");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    broadcastListOfActiveUsers();
                }
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