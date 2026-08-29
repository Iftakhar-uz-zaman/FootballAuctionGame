/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// ServerConnection.java
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Team myTeam;
    private ArrayList<GameStateListener> listeners = new ArrayList<>();

public ServerConnection(GameStateListener initialListener) {
    listeners.add(initialListener);
}

public void addListener(GameStateListener listener) {
    listeners.add(listener);
}

public void removeListener(GameStateListener listener) {
    listeners.remove(listener);
}

    public interface GameStateListener {
        void onStateUpdate(GameStateUpdate update);
        void onResponse(ServerResponse response);
        void onDisconnected(String reason);
    }

 

    public boolean connect(String host, int port) {

        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public void login(String teamName, String password) {
        send(new LoginRequest(teamName, password));
    }

    public void sendBid() {
        send(new BidRequest());
    }

    public void sendDropPlayer(int playerId) {
        send(new DropPlayerRequest(playerId));
    }

    public void sendBuyPlayer(int playerId) {
        send(new BuyPlayerRequest(playerId));
    }

    public void sendRunLeague() {
        send(new RunLeagueRequest());
    }

    private void send(Message message) {

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            for (GameStateListener l : new ArrayList<>(listeners)) {
            l.onDisconnected("Lost connection to server.");
        }
        }
    }

    public void startListening() {

    Thread listenThread = new Thread(() -> {

        try {

            while (true) {

                Object received = in.readObject();

                if (received instanceof GameStateUpdate) {

                    for (GameStateListener l : new ArrayList<>(listeners)) {
                        l.onStateUpdate((GameStateUpdate) received);
                    }

                } else if (received instanceof ServerResponse) {

                    for (GameStateListener l : new ArrayList<>(listeners)) {
                        l.onResponse((ServerResponse) received);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            for (GameStateListener l : new ArrayList<>(listeners)) {
                l.onDisconnected("Server closed the connection.");
            }
        }
    });

    listenThread.setDaemon(true);
    listenThread.start();
}
}
