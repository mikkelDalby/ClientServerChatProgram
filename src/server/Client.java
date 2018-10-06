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
}