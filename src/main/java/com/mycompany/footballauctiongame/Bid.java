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

    public Bid(Team bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }

    public Team getBidder() {
        return bidder;
    }

    public double getAmount() {
        return amount;
    }

    public void setBidder(Team bidder) {
        this.bidder = bidder;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
