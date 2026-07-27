/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */

public class Bid {

    private Team bidder;
    private double amount;

    public Bid() {
        bidder = null;
        amount = 0;
    }

    public Bid(Team bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }

    public Team getBidder() {
        return bidder;
    }

    public void setBidder(Team bidder) {
        this.bidder = bidder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Method Overloading
    public void increaseBid() {
        amount += 10;
    }

    public void increaseBid(double increment) {
        amount += increment;
    }

    @Override
    public String toString() {

        if (bidder == null) {
            return "Current Bid: " + amount + " | No Highest Bidder";
        }

        return "Current Bid: " + amount +
               " | Highest Bidder: " +
               bidder.getTeamName();
    }
}

   
