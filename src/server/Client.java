package server;

import java.net.Socket;

public class Client extends Thread {
    private volatile Socket client;
    private volatile String username;
    private volatile int aliveTime = 0;

    public Client(Socket socket, String username){
        this.client = socket;
        this.username = username;
    }

    public void run(){
        System.out.println("client thread running on server for user: " + username);
    }

    public void resetAliveTime(){
        this.aliveTime = 0;
    }

    public void addSecondToAliveTime(){
        this.aliveTime++;
    }

    public int getAliveTime(){
        return aliveTime;
    }

    public String getUsername(){
        return username;
    }
}