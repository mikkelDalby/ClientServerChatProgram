package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ImAlive extends Thread {

    private Socket socket;
    private boolean isAlive = true;
    private PrintWriter networkOutput;

    public ImAlive(Socket socket){
        this.socket = socket;
    }

    public void run() {
        PrintWriter networkOutput;
        while (isAlive){
            try {
                networkOutput = new PrintWriter(socket.getOutputStream(), true);
                networkOutput.println("IMAV");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
