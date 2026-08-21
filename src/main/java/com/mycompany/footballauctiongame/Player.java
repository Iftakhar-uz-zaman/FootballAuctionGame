/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
//controls player info
public class Player extends Person{

    private int id;
    private String position;
    private int overall;
    private double basePrice;
    private double currentBid;
    private Team winningTeam;
    private boolean sold;

    public Player(int id, String name, String position, int overall, double basePrice) {
        this.id = id;
        super(name);
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

    //returns the team name who won the player purchase
    public Team getWinningTeam() {
        return winningTeam;
    }

    //checks if the player is sold or not
    public boolean isSold() {
        return sold;
    }

    //sets the current price for a player after every bid made
    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    //sets the team name who won the player purchase
    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    //marks the player as sold
    public void setSold(boolean sold) {
        this.sold = sold;
    }

    //resets the bid for a new player
    public void resetBid() {
        currentBid = basePrice;
        winningTeam = null;
    }

    public void resetBid(double startingPrice) {
        currentBid = startingPrice;
        winningTeam = null;
    }

    @Override
    public String getRole() {
        return "Player";
    }
    
    @Override
    public String toString() {
        return id + ". " + getName() + " | " + position + " | OVR " + overall + " | Base Price: " + basePrice;
    }
}
