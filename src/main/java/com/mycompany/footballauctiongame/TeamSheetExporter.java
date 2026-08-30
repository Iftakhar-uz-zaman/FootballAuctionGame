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
import java.io.File;
import java.util.ArrayList;

public class TeamSheetExporter {
    // writes the team info on a text file for a manager to download
    public static void exportTeamSheet(Team team, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("TEAM SHEET");
            writer.newLine();
            writer.write("Team: " + team.getTeamName());
            writer.newLine();
            writer.write("Manager: " + team.getManager().getName());
            writer.newLine();
            writer.write("Purse Remaining: " + team.getPurse());
            writer.newLine();
            writer.write("Players Dropped: " + team.getDroppedCount());
            writer.newLine();
            writer.write("Players Added: " + team.getAddedCount());
            writer.newLine();
            writer.write("Formation: " + (team.getFormation() == null ? "Not set" : team.getFormation()));
writer.newLine();

if (team.getFormation() != null) {

    Team.FormationSlot[] slots = Team.getFormationOptions().get(team.getFormation());
    ArrayList<Player> lineup = team.getLineup();

    for (int i = 0; i < slots.length; i++) {

        Player p = i < lineup.size() ? lineup.get(i) : null;
        writer.write(slots[i].role + ": " + (p == null ? "-" : p.getName()));
        writer.newLine();
    }
}

writer.newLine();
            writer.newLine();
            writer.write("Squad:");
            writer.newLine();
            writer.write(team.getPlayerNames());
        }
    }
}
