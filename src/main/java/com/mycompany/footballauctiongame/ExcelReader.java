/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */

import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    public static ArrayList<Player> loadPlayers(String fileName) {

        ArrayList<Player> players = new ArrayList<>();

        try {

            try (FileInputStream fis = new FileInputStream(fileName); Workbook workbook = new XSSFWorkbook(fis)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    
                    Row row = sheet.getRow(i);
                    
                    int id = (int) row.getCell(0).getNumericCellValue();
                    
                    String name = row.getCell(1).getStringCellValue();
                    
                    String position = row.getCell(2).getStringCellValue();
                    
                    int overall = (int) row.getCell(3).getNumericCellValue();
                    
                    double basePrice =
                            row.getCell(4).getNumericCellValue();
                    
                    Player player =
                            new Player(id, name, position, overall, basePrice);
                    
                    players.add(player);
                    
                }
                
            }

        }

        catch (IOException e) {
            System.out.println("Error reading Excel File.");
            e.printStackTrace();
        }

        return players;

    }

}