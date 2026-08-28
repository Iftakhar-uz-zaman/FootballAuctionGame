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
import java.util.HashSet;

//controls team info
public class Team implements java.io.Serializable{
    private String teamName;
    private Manager manager;
    private double purse;
    private ArrayList<Player> squad;
    private int droppedCount = 0;
    private int addedCount = 0;
    private int fanHappiness = 50;
    
    //returns team chemistry: a parameter which will be used for runnig league simulation
    public int getChemistry() {
    if (squad.isEmpty()) {
        return 50;
    }
    //team chemistry increases with every positon that is covered (i.e. if a team is missing gk and they add their 1st GK to their squad the chemistry will increase by 5)
    HashSet<String> positionsCovered = new HashSet<>();
    for (Player p : squad) {
        positionsCovered.add(p.getPosition());
    }
    int chemistry = 50 + (positionsCovered.size() * 5);
    chemistry -= droppedCount * 3;
    if (chemistry > 100) {
        chemistry = 100;
    }
    if (chemistry < 0) {
        chemistry = 0;
    }
    return chemistry;
    }
    
    public int getFanHappiness() {    
        return fanHappiness;
    }

    //initializes fan happiness condition : another parameter which is being used in league simulation
    public void initializeFanHappiness() {   
        if (squad.isEmpty()) {       
            fanHappiness = 50;        
            return;    
        }   
        int totalOverall = 0;   
        for (Player p : squad) {        
            totalOverall += p.getOverall();    
        }    
        fanHappiness = totalOverall / squad.size();   
        if (fanHappiness > 100) {        
            fanHappiness = 100;    
        }    
        if (fanHappiness < 0) {        
            fanHappiness = 0;    
        }
    }

    public void adjustFanHappiness(int delta) {    
        fanHappiness += delta;
        if (fanHappiness > 100) {        
            fanHappiness = 100;    
        }    
        if (fanHappiness < 0) {
            fanHappiness = 0;    
        }
    }

    //number of players dropped from a team after the auction finishes
    public int getDroppedCount() {
        return droppedCount;
    }

    //number of players added in a team after the auction finishes
    public int getAddedCount() {    
        return addedCount;
    }

    //checks if the team is eligible to add new player after auction( a player can only add same number of players they have dropped)
    public boolean canAddPlayer() {    
        return addedCount < droppedCount;
    }

    //adjustments after dropping a player
    public void dropPlayer(Player player) throws PlayerNotFoundException {    
        if (!squad.contains(player)) {        
            throw new PlayerNotFoundException(player.getName() + " is not in " + teamName + "'s squad.");    
        }    
        double refund = player.getCurrentBid() * 0.5;   
        purse += refund;    
        squad.remove(player);   
        droppedCount++;   
        player.setWinningTeam(null);    
        player.setSold(false);    
        player.resetBid();
    }

    //runs while attempting to add player
    public void addPlayer(Player player, double price) throws SwapLimitExceededException, InsufficientPurseException {
        if (!canAddPlayer()) {        
            throw new SwapLimitExceededException(teamName + " cannot buy more players than it has dropped (" + droppedCount + " dropped, " + addedCount + " added).");    
        }    
        if (!canBid(price)) {       
            throw new InsufficientPurseException(teamName + " does not have enough purse to buy " + player.getName() + "." );   
        }    
        buyPlayer(player, price);    
        addedCount++;
    }

    //stores a teams components
    public Team(String teamName, Manager manager, double purse) {
        this.teamName = teamName;
        this.manager = manager;
        this.purse = purse;
        this.squad = new ArrayList<>();
    }

    public String getTeamName() {
        return teamName;
    }

    public Manager getManager() {
        return manager;
    }

    public double getPurse() {
        return purse;
    }

    public ArrayList<Player> getSquad() {
        return squad;
    }

    public void setPurse(double purse) {
        this.purse = purse;
    }

    //condition to be eligible to bid
    public boolean canBid(double amount) {
        return purse >= amount;
    }

    //runs while buying a player
    public void buyPlayer(Player player, double price) {
        purse -= price;
        squad.add(player);
        player.setWinningTeam(this);
        player.setSold(true);
        player.setCurrentBid(price);
    }

    //returns player details
    public String getPlayerNames() {    
        if (squad.isEmpty()) {        
            return "";    
        }   
        StringBuilder players = new StringBuilder();   
        for (Player player : squad) {       
            players.append(player.getName()).append("        ").append(player.getCurrentBid()).append("\n");   
        }    
        return players.toString();
    }

    //returns the sum of total spending of a team
    public double getTotalSquadValue() {
        double total = 0;
        for (Player player : squad) {
            total += player.getCurrentBid();
        }
        return total;
    }
    
    //measures team strength for running simulation
    public double getTeamStrength() {    
        if (squad.isEmpty()) {       
            return 0;    
        }    
        int totalOverall = 0;   
        for (Player p : squad) {       
            totalOverall += p.getOverall();   
        }    
        double avgOverall = (double) totalOverall / squad.size();    
        return (avgOverall * 0.6) + (getChemistry() * 0.2) + (getFanHappiness() * 0.2);
    }
    
    @Override
    public String toString() {
        return teamName + " | Manager: " + manager.getName() + " | Purse: " + purse;
    }
}
