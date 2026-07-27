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

    @Override
    public String toString() {

        return teamName +
                " | Manager: " + manager.getName() +
                " | Purse: " + purse;

    }

}
