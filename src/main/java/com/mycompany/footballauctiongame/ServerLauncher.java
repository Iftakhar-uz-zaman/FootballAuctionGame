/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// ServerLauncher.java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ServerLauncher {

    public static void main(String[] args) {

        ArrayList<Team> teams = loadTeamsFromFile("team_setup.txt");

        if (teams.isEmpty()) {
            System.out.println("No valid teams found in teams.txt. Exiting.");
            return;
        }

        ArrayList<Player> players = ExcelReader.loadPlayers("players.xlsx");

        if (players.isEmpty()) {
            System.out.println("No players found in players.xlsx. Exiting.");
            return;
        }

        AuctionEngine engine = new AuctionEngine(players, teams);
        GameServer server = new GameServer(engine);
        server.start();
    }

    private static ArrayList<Team> loadTeamsFromFile(String fileName) {

        ArrayList<Team> teams = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 4) {
                    System.out.println("Skipping malformed line " + lineNumber);
                    continue;
                }

                try {

                    String teamName = parts[0].trim();
                    String managerName = parts[1].trim();
                    double purse = Double.parseDouble(parts[2].trim());
                    String password = parts[3].trim();

                    teams.add(new Team(teamName, new Manager(managerName, teamName, password), purse));

                } catch (NumberFormatException e) {
                    System.out.println("Invalid purse on line " + lineNumber);
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read teams.txt: " + e.getMessage());
        }

        return teams;
    }
}
