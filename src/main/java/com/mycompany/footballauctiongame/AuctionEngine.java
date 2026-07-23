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
import java.util.Scanner;
import java.util.ArrayList;
public class AuctionEngine {
    private ArrayList<Team> activeTeams;
    private Bid currentBid;
    private ArrayList<Player> players;
    private ArrayList<Team> teams;
    private Scanner input;

    public AuctionEngine(ArrayList<Player> players,
                         ArrayList<Team> teams) {

        this.players = players;
        this.teams = teams;
        input = new Scanner(System.in);

    }
    private void initializeActiveTeams() {

    activeTeams = new ArrayList<>();

    for (Team t : teams) {
        activeTeams.add(t);
    }

}
    private boolean auctionFinished() {

    return activeTeams.size() <= 1;

}
    private void pass(Team team) {

    activeTeams.remove(team);

}
    private void placeBid(Team team, double amount) {

    currentBid.setAmount(amount);

    currentBid.setBidder(team);

}
    private void sellPlayer(Player player) {

    Team winner = currentBid.getBidder();

    if (winner == null) {

        System.out.println(player.getName() + " UNSOLD");

        return;
    }

    winner.buyPlayer(player, currentBid.getAmount());

    System.out.println("\n***********************");
    System.out.println("SOLD!");
    System.out.println(player.getName());

    System.out.println("Team : "
            + winner.getTeamName());

    System.out.println("Price : "
            + currentBid.getAmount());

    System.out.println("***********************");

}
    
    // Display all teams
    public void showTeams() {

        System.out.println("\n========== Teams ==========");

        for (int i = 0; i < teams.size(); i++) {

            Team t = teams.get(i);

            System.out.println((i + 1) + ". "
                    + t.getTeamName()
                    + " | Purse : "
                    + t.getPurse());

        }

    }

    // Update purse before auction
    public void updatePurse() {

        System.out.println("\nUpdate Team Purse");

        for (Team t : teams) {

            System.out.print(
                    t.getTeamName()
                    + " New Purse : ");

            double purse = input.nextDouble();

            t.setPurse(purse);

        }

    }
    public void startAuction() {

    for(Player player : players){

        auctionPlayer(player);

    }

    System.out.println("\nAuction Finished.");
    }
    private void auctionPlayer(Player player) {

    initializeBid(player);
    initializeActiveTeams();

    while (!auctionFinished()) {

        // Use a copy because activeTeams may change while looping
        ArrayList<Team> currentRound = new ArrayList<>(activeTeams);

        for (Team team : currentRound) {

            if (!activeTeams.contains(team))
                continue;

            showAuctionStatus(player);

            System.out.println("\nTeam : " + team.getTeamName());
            System.out.println("Remaining Purse : " + team.getPurse());

            System.out.println("\n1. Increase Bid (+5)");
            System.out.println("2. Increase Bid (+10)");
            System.out.println("3. Increase Bid (+20)");
            System.out.println("4. Pass");

            System.out.print("Choice : ");

            int choice = input.nextInt();

            if (choice == 4) {

                pass(team);

                System.out.println(team.getTeamName() + " Passed.");

                continue;
            }

            double increase = 0;

            switch (choice) {

                case 1:
                    increase = 5;
                    break;

                case 2:
                    increase = 10;
                    break;

                case 3:
                    increase = 20;
                    break;

                default:
                    System.out.println("Invalid Choice.");
                    continue;

            }

            double newBid = currentBid.getAmount() + increase;

            if (!team.canBid(newBid)) {

                System.out.println("Insufficient Purse.");
                pass(team);

                continue;

            }

            placeBid(team, newBid);

            System.out.println(team.getTeamName()
                    + " bids "
                    + newBid);

        }

    }

    sellPlayer(player);

}
    private void initializeBid(Player player) {

    currentBid = new Bid(null, player.getBasePrice());

}
    private void showAuctionStatus(Player player) {

    System.out.println("\n==============================");

    System.out.println("Player : " + player.getName());

    System.out.println("Position : " + player.getPosition());

    System.out.println("Overall : " + player.getOverall());

    System.out.println("Current Bid : " + currentBid.getAmount());

    if(currentBid.getBidder()==null){

        System.out.println("Highest Bidder : None");

    }

    else{

        System.out.println("Highest Bidder : "
                + currentBid.getBidder().getTeamName());

    }

    System.out.println("==============================");

}
}
