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

public class Team {

    private String teamName;
    private Manager manager;
    private double purse;
    private ArrayList<Player> squad;
    private int droppedCount = 0;
private int addedCount = 0;
private int fanHappiness = 50;

public int getChemistry() {

    if (squad.isEmpty()) {
        return 50;
    }

    java.util.HashSet<String> positionsCovered = new java.util.HashSet<>();

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

public int getDroppedCount() {
    return droppedCount;
}

public int getAddedCount() {
    return addedCount;
}

public boolean canAddPlayer() {
    return addedCount < droppedCount;
}

public void dropPlayer(Player player) throws PlayerNotFoundException {

    if (!squad.contains(player)) {
        throw new PlayerNotFoundException(
                player.getName() + " is not in " + teamName + "'s squad."
        );
    }

    double refund = player.getCurrentBid() * 0.5;

    purse += refund;
    squad.remove(player);
    droppedCount++;

    player.setWinningTeam(null);
    player.setSold(false);
    player.resetBid();
}

public void addPlayer(Player player, double price) throws SwapLimitExceededException, InsufficientPurseException {

    if (!canAddPlayer()) {
        throw new SwapLimitExceededException(
                teamName + " cannot buy more players than it has dropped ("
                + droppedCount + " dropped, " + addedCount + " added)."
        );
    }

    if (!canBid(price)) {
        throw new InsufficientPurseException(
                teamName + " does not have enough purse to buy "
                + player.getName() + "."
        );
    }

    buyPlayer(player, price); // your existing method — reused, not duplicated
    addedCount++;
}

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

    public boolean canBid(double amount) {
        return purse >= amount;
    }

    public void buyPlayer(Player player, double price) {

        purse -= price;

        squad.add(player);

        player.setWinningTeam(this);

        player.setSold(true);

        player.setCurrentBid(price);

    }

    public String getPlayerNames() {

    if (squad.isEmpty()) {
        return "";
    }

    StringBuilder players = new StringBuilder();

    for (Player player : squad) {

        players.append(player.getName())
               .append("        ")
               .append(player.getCurrentBid())
               .append("\n");

    }

    return players.toString();

}

    public double getTotalSquadValue() {

        double total = 0;

        for (Player player : squad) {

            total += player.getCurrentBid();

        }

        return total;

    }
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

        return teamName +
                " | Manager: " + manager.getName() +
                " | Purse: " + purse;

    }

}
