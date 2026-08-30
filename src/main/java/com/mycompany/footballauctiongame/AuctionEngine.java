/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//it controls all the action while the auction is running
public class AuctionEngine {
    private ArrayList<Player> players;
    private ArrayList<Team> teams;
    private ArrayList<Player> availablePlayers;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private Bid currentBid;
    private static final int BID_DURATION_SECONDS = 15;
    private java.util.HashSet<Team> passedTeams = new java.util.HashSet<>();
    private int secondsRemaining;
    private boolean paused = false;
    private String lastPassingTeam = "None";
    private int resolvedCount = 0;
private String lastResolvedPlayerName = null;
private String lastResolvedWinnerName = null;
private double lastResolvedPrice = 0;

public synchronized ServerResponse togglePause() {

    paused = !paused;

    return new ServerResponse(true, paused ? "Auction paused." : "Auction resumed.");
}

public boolean isPaused() {
    return paused;
}
    
    public synchronized ServerResponse pass(Team team) {

    if (auctionFinished()) {
        return new ServerResponse(false, "Auction has finished.");
    }

    if (team.equals(currentBid.getBidder())) {
        return new ServerResponse(false, "You're the highest bidder — you can't pass.");
    }

    passedTeams.add(team);
    lastPassingTeam = team.getTeamName();
    boolean everyoneElsePassedTheBidder =
            currentBid.getBidder() != null && passedTeams.size() >= teams.size() - 1;

    boolean everyonePassedWithNoBid =
            currentBid.getBidder() == null && passedTeams.size() >= teams.size();

    if (everyoneElsePassedTheBidder || everyonePassedWithNoBid) {
        resolveCurrentPlayer();
    }

    return new ServerResponse(true, team.getTeamName() + " passed.");
}

    public String getLastPassingTeam() {
    return lastPassingTeam;
}

public int getPassedCount() {
    return passedTeams.size();
}

public synchronized ServerResponse bid(Team team) {

    if (auctionFinished()) {
        return new ServerResponse(false, "Auction has finished.");
    }

    if (passedTeams.contains(team)) {
        return new ServerResponse(false, "You already passed on this player.");
    }

    boolean isFirstBid = currentBid.getBidder() == null;
    double nextPrice = isFirstBid ? currentBid.getAmount() : currentBid.getAmount() + 10;

    if (!team.canBid(nextPrice)) {
        return new ServerResponse(false, "Not enough purse.");
    }

    if (!isFirstBid) {
        currentBid.increaseBid();
    }

    currentBid.setBidder(team);

    currentPlayer.setCurrentBid(currentBid.getAmount());
    currentPlayer.setWinningTeam(team);

    secondsRemaining = BID_DURATION_SECONDS;

    double confirmedAmount = currentBid.getAmount();   // NEW — captured before anything can null it out

    if (noOneElseCanContest(team)) {
        resolveCurrentPlayer();
    }

    return new ServerResponse(true, team.getTeamName() + " bids " + confirmedAmount);   // CHANGED
}

private boolean noOneElseCanContest(Team currentBidder) {

    double nextRequiredBid = currentBid.getAmount() + 10;

    for (Team team : teams) {

        if (team.equals(currentBidder)) {
            continue;
        }

        boolean hasPassed = passedTeams.contains(team);
        boolean canAffordNext = team.canBid(nextRequiredBid);

        if (!hasPassed && canAffordNext) {
            return false; // this team could still contest — don't resolve yet
        }
    }

    return true; // every other team has either passed or can't afford to continue
}



public int getSecondsRemaining() {
    return secondsRemaining;
}

public void tickCountdown() {

    secondsRemaining--;

    if (secondsRemaining <= 0) {
        resolveCurrentPlayer();
    }
}

private void resolveCurrentPlayer() {

    resolvedCount++;
    lastResolvedPlayerName = currentPlayer.getName();

    if (currentBid.getBidder() != null) {

        lastResolvedWinnerName = currentBid.getBidder().getTeamName();
        lastResolvedPrice = currentBid.getAmount();
        sellPlayer();

    } else {

        lastResolvedWinnerName = null;
        lastResolvedPrice = 0;
        availablePlayers.add(currentPlayer);
        nextPlayer();
    }

    if (!auctionFinished()) {
        secondsRemaining = BID_DURATION_SECONDS;
    }
}

public int getResolvedCount() { return resolvedCount; }
public String getLastResolvedPlayerName() { return lastResolvedPlayerName; }
public String getLastResolvedWinnerName() { return lastResolvedWinnerName; }
public double getLastResolvedPrice() { return lastResolvedPrice; }

    //runs the auction
    public AuctionEngine(ArrayList<Player> players, ArrayList<Team> teams) {
        this.players = players;
        this.teams = teams;
        currentPlayerIndex = 0;
        availablePlayers = new ArrayList<>();
        if (!players.isEmpty()) {
            currentPlayer = players.get(0);
            currentBid = new Bid(null,currentPlayer.getBasePrice());
        }
        secondsRemaining = BID_DURATION_SECONDS;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    //returns the condition in refresh screen method in main frame when auction is finished
    public boolean auctionFinished() {
        return currentPlayerIndex >= players.size();
    }

    
    public ArrayList<Player> getAvailablePlayers() {
        return availablePlayers;
    }
    
    //drops a player from team after auction
    public void dropPlayerFromTeam(Team team, Player player) throws PlayerNotFoundException {   
        team.dropPlayer(player);   
        availablePlayers.add(player);
    }
    
    //adds a player after auction
    public void buyAvailablePlayer(Team team, Player player) throws SwapLimitExceededException, InsufficientPurseException, PlayerNotFoundException {   
        if (!availablePlayers.contains(player)) {       
            throw new PlayerNotFoundException(player.getName() + " is not available to buy.");   
        }    
        team.addPlayer(player, player.getBasePrice());    
        availablePlayers.remove(player);
    }
    
    //runs when a team wins a bid or every team passes
    private void sellPlayer() {
        Team winner = currentBid.getBidder();
        if (winner != null) {
            winner.buyPlayer(currentPlayer,currentBid.getAmount());
            System.out.println();
            
            System.out.println("Player : " + currentPlayer.getName());
            System.out.println("Winner : " + winner.getTeamName());
            System.out.println("Price : " + currentBid.getAmount());
            TextFileManager.saveTeams(teams);
        }
        nextPlayer();
    }

    //proceeds with the next player
    private void nextPlayer() {
        currentPlayerIndex++;
        passedTeams.clear();
        if (currentPlayerIndex >= players.size()) {
            currentPlayer = null;
            currentBid = null;
            return;
        }
        currentPlayer = players.get(currentPlayerIndex);
        currentBid = new Bid(null,currentPlayer.getBasePrice());
    }

    //shows ongoing player who is being auctioned
    public void showCurrentPlayer() {
        if (currentPlayer == null) {
            System.out.println("Auction Finished!");
            return;
        }
        System.out.println("-----------------------------------");
        System.out.println("Player : " + currentPlayer.getName());
        System.out.println("Position : " + currentPlayer.getPosition());
        System.out.println("Overall : " + currentPlayer.getOverall());
        System.out.println("Base Price : " + currentPlayer.getBasePrice());
        System.out.println("Current Bid : " + currentBid.getAmount());
        if (currentBid.getBidder() == null) {
            System.out.println("Highest Bidder : None");
        }
        else {
            System.out.println("Highest Bidder : " + currentBid.getBidder().getTeamName());
        }
        System.out.println();
        System.out.println("-----------------------------------");
    }
}
