/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// GameServer.java
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer {

    private static final int PORT = 5555;

    private AuctionEngine engine;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private Object lock = new Object();

    public GameServer(AuctionEngine engine) {
        this.engine = engine;
    }

    public AuctionEngine getEngine() {
        return engine;
    }

    public Team authenticate(String teamName, String password) {

        for (Team team : engine.getTeams()) {

            if (team.getTeamName().equals(teamName)
                    && team.getManager().checkPassword(password)) {

                return team;
            }
        }

        return null;
    }

    public Player findPlayerById(ArrayList<Player> players, int id) throws PlayerNotFoundException {

        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }

        throw new PlayerNotFoundException("Player with id " + id + " not found.");
    }

    public void removeClient(ClientHandler client) {

        synchronized (lock) {
            clients.remove(client);
        }
    }

    public void broadcastState() {

        GameStateUpdate update = buildStateUpdate();

        synchronized (lock) {
            for (ClientHandler client : clients) {
                client.sendMessage(update);
            }
        }
    }

    private GameStateUpdate buildStateUpdate() {

        String bidderName = engine.getCurrentBid() == null || engine.getCurrentBid().getBidder() == null
                ? "None"
                : engine.getCurrentBid().getBidder().getTeamName();

        return new GameStateUpdate(
                engine.getTeams(),
                engine.getAvailablePlayers(),
                engine.getCurrentPlayer(),
                engine.getCurrentBid() == null ? 0 : engine.getCurrentBid().getAmount(),
                bidderName,
                engine.getSecondsRemaining(),
                engine.auctionFinished()
        );
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started on port " + PORT);

            startCountdownThread();

            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("New connection from " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket, this);

                synchronized (lock) {
                    clients.add(handler);
                }

                handler.start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private void startCountdownThread() {

    Thread countdownThread = new Thread(() -> {

        while (true) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }

            boolean allTeamsPresent;

            synchronized (lock) {
                allTeamsPresent = clients.size() >= engine.getTeams().size();
            }

            if (allTeamsPresent && !engine.auctionFinished()) {
                engine.tickCountdown();
                broadcastState();
            }
        }
    });

    countdownThread.setDaemon(true);
    countdownThread.start();
}
}