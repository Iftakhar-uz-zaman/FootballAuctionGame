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

public class AuctionEngine {

    private ArrayList<Player> players;
    private ArrayList<Team> teams;

    private int currentPlayerIndex;
    private int currentTeamIndex;
    private int passCount;

    private Player currentPlayer;
    private Bid currentBid;

    public AuctionEngine(ArrayList<Player> players, ArrayList<Team> teams) {

        this.players = players;
        this.teams = teams;

        currentPlayerIndex = 0;
        currentTeamIndex = 0;
        passCount = 0;

        if (!players.isEmpty()) {

            currentPlayer = players.get(0);

            currentBid = new Bid(
                    null,
                    currentPlayer.getBasePrice()
            );

        }

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public Team getCurrentTeam() {
        return teams.get(currentTeamIndex);
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public boolean auctionFinished() {
        return currentPlayerIndex >= players.size();
    }

    private void nextTeam() {

        currentTeamIndex++;

        if (currentTeamIndex >= teams.size()) {
            currentTeamIndex = 0;
        }

    }

    public boolean bid() {

        Team team = getCurrentTeam();

        double nextPrice = currentBid.getAmount() + 10;

        if (!team.canBid(nextPrice)) {

            javax.swing.JOptionPane.showMessageDialog(
        null,
        team.getTeamName() + " does not have enough purse.",
        "Insufficient Funds",
        javax.swing.JOptionPane.WARNING_MESSAGE
);

            nextTeam();

            return false;

        }

        currentBid.increaseBid();

        currentBid.setBidder(team);

        currentPlayer.setCurrentBid(currentBid.getAmount());

        currentPlayer.setWinningTeam(team);

        passCount = 0;

        nextTeam();

        return true;

    }

    public boolean pass() {

        passCount++;

        // Everyone passed without any bid
        if (passCount >= teams.size()
                && currentBid.getBidder() == null) {

            System.out.println(currentPlayer.getName()
                    + " remained UNSOLD.");

            nextPlayer();

            return true;

        }

        // Everyone except highest bidder passed
        if (currentBid.getBidder() != null
                && passCount >= teams.size() - 1) {

            sellPlayer();

            return true;

        }

        nextTeam();

        return false;

    }

    private void sellPlayer() {

        Team winner = currentBid.getBidder();

        if (winner != null) {

            winner.buyPlayer(
                    currentPlayer,
                    currentBid.getAmount()
            );

            System.out.println();

            javax.swing.JOptionPane.showMessageDialog(
        null,
        currentPlayer.getName()
        + "\nSold To : "
        + winner.getTeamName()
        + "\nPrice : "
        + currentBid.getAmount(),
        "Player Sold",
        javax.swing.JOptionPane.INFORMATION_MESSAGE
);

            System.out.println("Player : "
                    + currentPlayer.getName());

            System.out.println("Winner : "
                    + winner.getTeamName());

            System.out.println("Price : "
                    + currentBid.getAmount());

            TextFileManager.saveTeams(teams);

        }

        nextPlayer();

    }

    private void nextPlayer() {

        currentPlayerIndex++;

        if (currentPlayerIndex >= players.size()) {

            currentPlayer = null;

            currentBid = null;

            return;

        }

        currentPlayer = players.get(currentPlayerIndex);

        currentBid = new Bid(
                null,
                currentPlayer.getBasePrice()
        );

        currentTeamIndex = 0;

        passCount = 0;

    }

    public void showCurrentPlayer() {

        if (currentPlayer == null) {

            System.out.println("Auction Finished!");

            return;

        }

        System.out.println("-----------------------------------");

        System.out.println("Player : "
                + currentPlayer.getName());

        System.out.println("Position : "
                + currentPlayer.getPosition());

        System.out.println("Overall : "
                + currentPlayer.getOverall());

        System.out.println("Base Price : "
                + currentPlayer.getBasePrice());

        System.out.println("Current Bid : "
                + currentBid.getAmount());

        if (currentBid.getBidder() == null) {

            System.out.println("Highest Bidder : None");

        } else {

            System.out.println("Highest Bidder : "
                    + currentBid.getBidder().getTeamName());

        }

        System.out.println();

        System.out.println("Current Turn : "
                + getCurrentTeam().getTeamName());

        System.out.println("-----------------------------------");

    }

}
