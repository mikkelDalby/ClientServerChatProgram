package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

// Sends a heartbeat to the server once every 60 seconds
public class ImAlive extends Thread {

    private Socket socket;
    private boolean isAlive = true;
    private PrintWriter networkOutput;

    public ImAlive(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try {
            networkOutput = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (isAlive){
            networkOutput.println("IMAV");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
