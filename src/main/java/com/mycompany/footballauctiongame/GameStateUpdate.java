/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// GameStateUpdate.java
import java.util.ArrayList;

public class GameStateUpdate extends Message {

    private ArrayList<Team> teams;
    private ArrayList<Player> availablePlayers;
    private Player currentPlayer;
    private double currentBidAmount;
    private String currentBidderName;
    private int secondsRemaining;
    private boolean auctionFinished;

    public GameStateUpdate(ArrayList<Team> teams, ArrayList<Player> availablePlayers,
            Player currentPlayer, double currentBidAmount, String currentBidderName,
            int secondsRemaining, boolean auctionFinished) {

        this.teams = teams;
        this.availablePlayers = availablePlayers;
        this.currentPlayer = currentPlayer;
        this.currentBidAmount = currentBidAmount;
        this.currentBidderName = currentBidderName;
        this.secondsRemaining = secondsRemaining;
        this.auctionFinished = auctionFinished;
    }

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

    @Override
    public String describe() {
        return "Game state update";
    }
}