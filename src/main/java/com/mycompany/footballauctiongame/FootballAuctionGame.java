/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */

import java.util.ArrayList;
import java.util.Scanner;

public class FootballAuctionGame {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Load players from Excel
        ArrayList<Player> players =
                ExcelReader.loadPlayers("players.xlsx");

        if(players.isEmpty()){

            System.out.println("No players found!");
            return;

        }

        ArrayList<Team> teams = new ArrayList<>();

        System.out.print("Enter Number of Teams : ");
        int numberOfTeams = input.nextInt();
        input.nextLine();

        for(int i=1;i<=numberOfTeams;i++){

            System.out.println("\nTeam " + i);

            System.out.print("Team Name : ");
            String teamName = input.nextLine();

            System.out.print("Manager Name : ");
            String managerName = input.nextLine();

            System.out.print("Purse : ");
            double purse = input.nextDouble();
            input.nextLine();

            Manager manager =
                    new Manager(managerName, teamName);

            Team team =
                    new Team(teamName, manager, purse);

            teams.add(team);

        }

        AuctionEngine engine =
                new AuctionEngine(players, teams);

        System.out.print("\nDo you want to update purse before auction? (Y/N): ");

        String choice = input.nextLine();

        if(choice.equalsIgnoreCase("Y")){

            engine.updatePurse();

        }

        engine.showTeams();

        System.out.println("\nPress Enter to Start Auction...");
        input.nextLine();

        engine.startAuction();

        System.out.println("\n==========================");
        System.out.println("FINAL SQUADS");
        System.out.println("==========================");

        for(Team t : teams){

            t.showSquad();

        }

    }

}
