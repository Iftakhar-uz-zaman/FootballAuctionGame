/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextFileManager {
    public static void saveTeams(ArrayList<Team> teams) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("teams.txt"))) {
            for (Team team : teams) {
                writer.write(team.getTeamName() + "|" + team.getManager().getName() + "|" + team.getPurse() + "|" + team.getPlayerNames());
                writer.newLine();

            }
            System.out.println("Teams saved successfully.");
        }
        catch (IOException e) {
            System.out.println("Error saving teams.");
            e.printStackTrace();
        }
    }
}