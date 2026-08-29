/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// ClientHandler.java
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameServer server;
    private Team myTeam;

    public ClientHandler(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    public Team getMyTeam() {
        return myTeam;
    }

    public void sendMessage(Message message) {

    try {
        out.writeObject(message);
        out.flush();
    } catch (IOException e) {

        System.out.println((myTeam == null ? "A client" : myTeam.getTeamName())
                + " appears disconnected — removing.");

        server.removeClient(this);

        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}

    @Override
    public void run() {

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // First message from any client must be a login
            Object first = in.readObject();

            if (!(first instanceof LoginRequest)) {
                sendMessage(new ServerResponse(false, "Expected login."));
                socket.close();
                return;
            }

            LoginRequest login = (LoginRequest) first;
            myTeam = server.authenticate(login.getTeamName(), login.getPassword());

            if (myTeam == null) {
                sendMessage(new ServerResponse(false, "Invalid team name or password."));
                socket.close();
                return;
            }

            sendMessage(new ServerResponse(true, "Logged in as " + myTeam.getTeamName()));
            server.broadcastState();

            // Main loop: keep handling requests until disconnect
            while (true) {

                Object message = in.readObject();
                handleMessage(message);
            }

        } catch (EOFException | java.net.SocketException e) {
            System.out.println((myTeam == null ? "A client" : myTeam.getTeamName()) + " disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection error: " + e.getMessage());
        } finally {
            server.removeClient(this);
        }
    }

    private void handleMessage(Object message) {

        if (message instanceof BidRequest) {

            ServerResponse response = server.getEngine().bid(myTeam);
            sendMessage(response);
            server.broadcastState();

        }
        else if (message instanceof PassRequest) {

    ServerResponse response = server.getEngine().pass(myTeam);
    sendMessage(response);
    server.broadcastState();
        }
        
        else if (message instanceof TogglePauseRequest) {

    ServerResponse response = server.getEngine().togglePause();
    sendMessage(response);
    server.broadcastState();

}
        else if (message instanceof DropPlayerRequest) {

            DropPlayerRequest req = (DropPlayerRequest) message;

            try {
                Player player = server.findPlayerById(myTeam.getSquad(), req.getPlayerId());
                server.getEngine().dropPlayerFromTeam(myTeam, player);
                sendMessage(new ServerResponse(true, player.getName() + " dropped."));
            } catch (PlayerNotFoundException e) {
                sendMessage(new ServerResponse(false, e.getMessage()));
            }

            server.broadcastState();

        } else if (message instanceof BuyPlayerRequest) {

            BuyPlayerRequest req = (BuyPlayerRequest) message;

            try {
                Player player = server.findPlayerById(
                        server.getEngine().getAvailablePlayers(), req.getPlayerId());
                server.getEngine().buyAvailablePlayer(myTeam, player);
                sendMessage(new ServerResponse(true, player.getName() + " bought."));
            } catch (PlayerNotFoundException | SwapLimitExceededException
                    | InsufficientPurseException e) {
                sendMessage(new ServerResponse(false, e.getMessage()));
            }

            server.broadcastState();

        } else if (message instanceof RunLeagueRequest) {

            String results = server.getEngine().simulateLeague();
            sendMessage(new ServerResponse(true, results));
        }
    }
}
