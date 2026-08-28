/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
//it control what to do when a bid is placed
public class Bid implements java.io.Serializable{
    private Team bidder = null;
    private double amount = 0;

    public Bid(Team bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }

    //gets the bidding team name
    public Team getBidder() {
        return bidder;
    }

    //sets the bidding team name
    public void setBidder(Team bidder) {
        this.bidder = bidder;
    }

    //gets the price of a player
    public double getAmount() {
        return amount;
    }

    //sets the price of a player
    public void setAmount(double amount) {
        this.amount = amount;
    }

    //increases the bid by 10
    public void increaseBid() {
        amount += 10;
    }

    //sets the new amount after increment
    public void increaseBid(double increment) {
        amount += increment;
    }

    @Override
    public String toString() {
        if (bidder == null) {
            return "Current Bid: " + amount + " | No Highest Bidder";
        }
        return "Current Bid: " + amount + " | Highest Bidder: " + bidder.getTeamName();
    }
}

   
