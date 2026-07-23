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

    // Getters
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

    // Setters
    public void setPurse(double purse) {
        this.purse = purse;
    }

    // Add player to team
    public void buyPlayer(Player player, double bidAmount) {

        purse -= bidAmount;

        player.setSold(true);
        player.setCurrentBid(bidAmount);
        player.setWinningTeam(this);

        squad.add(player);
    }

    // Check if team can afford a bid
    public boolean canBid(double amount) {
        return purse >= amount;
    }

    // Display squad
    public void showSquad() {

        System.out.println("\n==============================");
        System.out.println("Team : " + teamName);
        System.out.println("Manager : " + manager.getName());
        System.out.println("Remaining Purse : " + purse);
        System.out.println("------------------------------");

        if (squad.isEmpty()) {
            System.out.println("No Players Purchased.");
        } else {

            for (Player p : squad) {

                System.out.println(
                        p.getName()
                        + " ("
                        + p.getPosition()
                        + ") - "
                        + p.getCurrentBid());

            }

        }

        System.out.println("==============================");
    }
    public void showTeamInfo() {

    System.out.println("---------------------------");
    System.out.println("Team : " + teamName);
    System.out.println("Purse : " + purse);
    System.out.println("---------------------------");

}
}