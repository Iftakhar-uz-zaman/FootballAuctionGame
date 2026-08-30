/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
import java.util.ArrayList;

public class GameStateUpdate extends Message {

    private ArrayList<Team> teams;
    private ArrayList<Player> availablePlayers;
    private Player currentPlayer;
    private double currentBidAmount;
    private String currentBidderName;
    private int secondsRemaining;
    private boolean auctionFinished;
    private boolean paused;
    private boolean allTeamsConnected;
    private int passedCount;
    private String lastPassingTeam;
    private ArrayList<String> connectedTeamNames;
    private int resolvedCount = 0;
private String lastResolvedPlayerName = null;
private String lastResolvedWinnerName = null;
private double lastResolvedPrice = 0;

    public GameStateUpdate(ArrayList<Team> teams, ArrayList<Player> availablePlayers,
            Player currentPlayer, double currentBidAmount, String currentBidderName,
            int secondsRemaining, boolean auctionFinished, boolean paused,
            boolean allTeamsConnected, ArrayList<String> connectedTeamNames,
            int passedCount, String lastPassingTeam, int resolvedCount, String lastResolvedPlayerName, String lastResolvedWinnerName, double lastResolvedPrice) {

        this.teams = teams;
        this.availablePlayers = availablePlayers;
        this.currentPlayer = currentPlayer;
        this.currentBidAmount = currentBidAmount;
        this.currentBidderName = currentBidderName;
        this.secondsRemaining = secondsRemaining;
        this.auctionFinished = auctionFinished;
        this.paused = paused;
        this.allTeamsConnected = allTeamsConnected;
        this.connectedTeamNames = connectedTeamNames;
        this.passedCount = passedCount;
        this.lastPassingTeam = lastPassingTeam;
        this.resolvedCount = resolvedCount;
        this.lastResolvedPlayerName = lastResolvedPlayerName;
        this.lastResolvedWinnerName = lastResolvedWinnerName;
        this.lastResolvedPrice = lastResolvedPrice;
    }

    public int getResolvedCount() { return resolvedCount; }
public String getLastResolvedPlayerName() { return lastResolvedPlayerName; }
public String getLastResolvedWinnerName() { return lastResolvedWinnerName; }
public double getLastResolvedPrice() { return lastResolvedPrice; }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Player> getAvailablePlayers() {
        return availablePlayers;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public double getCurrentBidAmount() {
        return currentBidAmount;
    }

    public String getCurrentBidderName() {
        return currentBidderName;
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public boolean isAuctionFinished() {
        return auctionFinished;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isAllTeamsConnected() {
        return allTeamsConnected;
    }

    public ArrayList<String> getConnectedTeamNames() {
        return connectedTeamNames;
    }

    public int getPassedCount() {
        return passedCount;
    }

    public String getLastPassingTeam() {
        return lastPassingTeam;
    }

    @Override
    public String describe() {
        return "Game state update";
    }
}