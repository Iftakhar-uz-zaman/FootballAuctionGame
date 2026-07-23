/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */

public class Player {

    private int id;
    private String name;
    private String position;
    private int overall;
    private double basePrice;
    private double currentBid;
    private boolean sold;
    private Team winningTeam;

    public Player(int id, String name, String position, int overall, double basePrice) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.overall = overall;
        this.basePrice = basePrice;
        this.currentBid = basePrice;
        this.sold = false;
        this.winningTeam = null;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getOverall() {
        return overall;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public boolean isSold() {
        return sold;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    // Setters
    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public void displayPlayer() {
        System.out.println("----------------------------");
        System.out.println("ID          : " + id);
        System.out.println("Name        : " + name);
        System.out.println("Position    : " + position);
        System.out.println("Overall     : " + overall);
        System.out.println("Base Price  : " + basePrice);

        if (sold) {
            System.out.println("Current Bid : " + currentBid);
            System.out.println("Sold To     : " + winningTeam.getTeamName());
        } else {
            System.out.println("Status      : Unsold");
        }

        System.out.println("----------------------------");
    }
}
