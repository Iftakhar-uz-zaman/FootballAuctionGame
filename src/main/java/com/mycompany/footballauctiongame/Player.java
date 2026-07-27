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
    private Team winningTeam;
    private boolean sold;

    public Player(int id, String name, String position,
                  int overall, double basePrice) {

        this.id = id;
        this.name = name;
        this.position = position;
        this.overall = overall;
        this.basePrice = basePrice;

        this.currentBid = basePrice;
        this.winningTeam = null;
        this.sold = false;
    }

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

    public Team getWinningTeam() {
        return winningTeam;
    }

    public boolean isSold() {
        return sold;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    // Method Overloading
    public void resetBid() {
        currentBid = basePrice;
        winningTeam = null;
    }

    public void resetBid(double startingPrice) {
        currentBid = startingPrice;
        winningTeam = null;
    }

    @Override
    public String toString() {

        return id + ". " + name +
                " | " + position +
                " | OVR " + overall +
                " | Base Price: " + basePrice;

    }
}
