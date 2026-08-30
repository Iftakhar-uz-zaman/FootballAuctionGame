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
    
    public static class FormationSlot implements java.io.Serializable {

    public String role;
    public int xPercent;
    public int yPercent;

    public FormationSlot(String role, int xPercent, int yPercent) {
        this.role = role;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
    }
}

private String formation = null;
private ArrayList<Player> lineup = new ArrayList<>();

public String getFormation() {
    return formation;
}

public ArrayList<Player> getLineup() {
    return lineup;
}

public static java.util.LinkedHashMap<String, FormationSlot[]> getFormationOptions() {

    java.util.LinkedHashMap<String, FormationSlot[]> formations = new java.util.LinkedHashMap<>();

    formations.put("4-4-2", new FormationSlot[]{
        new FormationSlot("GK", 50, 90),
        new FormationSlot("LB", 15, 70), new FormationSlot("CB", 35, 72),
        new FormationSlot("CB", 65, 72), new FormationSlot("RB", 85, 70),
        new FormationSlot("LM", 15, 45), new FormationSlot("CM", 38, 48),
        new FormationSlot("CM", 62, 48), new FormationSlot("RM", 85, 45),
        new FormationSlot("ST", 38, 15), new FormationSlot("ST", 62, 15)
    });

    formations.put("4-3-3", new FormationSlot[]{
        new FormationSlot("GK", 50, 90),
        new FormationSlot("LB", 15, 70), new FormationSlot("CB", 35, 72),
        new FormationSlot("CB", 65, 72), new FormationSlot("RB", 85, 70),
        new FormationSlot("CM", 30, 48), new FormationSlot("CM", 50, 50), new FormationSlot("CM", 70, 48),
        new FormationSlot("LW", 20, 15), new FormationSlot("ST", 50, 12), new FormationSlot("RW", 80, 15)
    });

    formations.put("3-5-2", new FormationSlot[]{
        new FormationSlot("GK", 50, 90),
        new FormationSlot("CB", 30, 72), new FormationSlot("CB", 50, 75), new FormationSlot("CB", 70, 72),
        new FormationSlot("LM", 12, 48), new FormationSlot("CM", 32, 50), new FormationSlot("CM", 50, 52),
        new FormationSlot("CM", 68, 50), new FormationSlot("RM", 88, 48),
        new FormationSlot("ST", 38, 15), new FormationSlot("ST", 62, 15)
    });

    return formations;
}

public void setFormation(String formationName) {

    this.formation = formationName;

    int slotCount = getFormationOptions().get(formationName).length;
    lineup = new ArrayList<>();

    for (int i = 0; i < slotCount; i++) {
        lineup.add(null);
    }
}

public void setLineupSlot(int slotIndex, Player player) throws PlayerNotFoundException {

    if (formation == null) {
        throw new PlayerNotFoundException("Select a formation first.");
    }

    if (player != null && !squad.contains(player)) {
        throw new PlayerNotFoundException(player.getName() + " is not in " + teamName + "'s squad.");
    }

    if (player != null) {
        for (int i = 0; i < lineup.size(); i++) {
            if (i != slotIndex && player.equals(lineup.get(i))) {
                lineup.set(i, null); // move the player instead of allowing duplicates
            }
        }
    }

    lineup.set(slotIndex, player);
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
        for (int i = 0; i < lineup.size(); i++) {
    if (player.equals(lineup.get(i))) {
        lineup.set(i, null);
    }
}
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
    
    @Override
    public String toString() {
        return teamName + " | Manager: " + manager.getName() + " | Purse: " + purse;
    }
}
