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
import java.util.HashMap;
import java.util.Random;

public class AuctionEngine {
    private ArrayList<Player> players;
    private ArrayList<Team> teams;
    private ArrayList<Player> availablePlayers;
    private String leagueResults = null;
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
        availablePlayers = new ArrayList<>();
        if (!players.isEmpty()) {
            currentPlayer = players.get(0);
            //goes to bid from here
            currentBid = new Bid(null,currentPlayer.getBasePrice());
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
        //goes to bid from here
        double nextPrice = currentBid.getAmount() + 10;
        if (!team.canBid(nextPrice)) {
            javax.swing.JOptionPane.showMessageDialog(null,team.getTeamName() + " does not have enough purse.","Insufficient Funds",javax.swing.JOptionPane.WARNING_MESSAGE);
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
        if (passCount >= teams.size() && currentBid.getBidder() == null) {
            System.out.println(currentPlayer.getName() + " remained UNSOLD.");
            availablePlayers.add(currentPlayer);
            nextPlayer();
            return true;
        }       
        // Everyone except highest bidder passed
        if (currentBid.getBidder() != null && passCount >= teams.size() - 1) {
            sellPlayer();
            return true;
        }
        nextTeam();
        return false;        
   }
    
    public ArrayList<Player> getAvailablePlayers() {
        return availablePlayers;
    }
    
    //drops a player from team
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
    
    //when a player is sold to a team
    private void sellPlayer() {
        Team winner = currentBid.getBidder();
        if (winner != null) {
            winner.buyPlayer(currentPlayer,currentBid.getAmount());
            System.out.println();
            javax.swing.JOptionPane.showMessageDialog(null,currentPlayer.getName() + "\nSold To : " + winner.getTeamName() + "\nPrice : " + currentBid.getAmount(),"Player Sold", javax.swing.JOptionPane.INFORMATION_MESSAGE);
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
        if (currentPlayerIndex >= players.size()) {
            currentPlayer = null;
            currentBid = null;
            for (Team team : teams) {
            team.initializeFanHappiness();
            }
            return;
        }
        currentPlayer = players.get(currentPlayerIndex);
        currentBid = new Bid(null,currentPlayer.getBasePrice());
        currentTeamIndex = 0;
        passCount = 0;
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
        System.out.println("Current Turn : " + getCurrentTeam().getTeamName());
        System.out.println("-----------------------------------");
    }

    //simulates league after team set up
    public String simulateLeague() {    
        if (leagueResults != null) {
        return leagueResults; //already run — return the same result   
        }   
        if (teams.size() < 2) {        
            return "Need at least 2 teams to run a league.";    
        }   
        HashMap<Team, Integer> points = new HashMap<>();   
        for (Team team : teams) {        
            points.put(team, 0);    
        }  
        Random random = new Random();  
        StringBuilder log = new StringBuilder();   
        log.append("=== MATCH RESULTS ===\n\n");
        // Round robin: every team plays every other team once       
        for (int i = 0; i < teams.size(); i++) {        
            for (int j = i + 1; j < teams.size(); j++) {           
                Team home = teams.get(i);           
                Team away = teams.get(j);           
                double homeScore = home.getTeamStrength() + random.nextInt(21) - 10;           
                double awayScore = away.getTeamStrength() + random.nextInt(21) - 10;           
                log.append(home.getTeamName()).append(" vs ").append(away.getTeamName()).append("  ->  ");           
                if (homeScore > awayScore) {               
                    points.put(home, points.get(home) + 3);               
                    home.adjustFanHappiness(5);               
                    away.adjustFanHappiness(-5);                
                    log.append(home.getTeamName()).append(" wins\n");            
                }
                else if (awayScore > homeScore) {               
                    points.put(away, points.get(away) + 3);                
                    away.adjustFanHappiness(5);                
                    home.adjustFanHappiness(-5);                
                    log.append(away.getTeamName()).append(" wins\n");           
                } 
                else {               
                    points.put(home, points.get(home) + 1);               
                    points.put(away, points.get(away) + 1);               
                    home.adjustFanHappiness(1);               
                    away.adjustFanHappiness(1);               
                    log.append("Draw\n");           
                }        
            }   
        }
        //shows team standings
        ArrayList<Team> standings = new ArrayList<>(teams);    
        standings.sort((t1, t2) -> points.get(t2) - points.get(t1));   
        log.append("\n=== FINAL STANDINGS ===\n\n");   
        int rank = 1;    
        for (Team team : standings) {      
            log.append(rank).append(". ").append(team.getTeamName()).append(" - ").append(points.get(team)).append(" pts  (Fan Happiness: ").append(team.getFanHappiness()).append(")\n");       
            rank++;   
        }   
        leagueResults = log.toString();   
        return leagueResults;
    }
}
