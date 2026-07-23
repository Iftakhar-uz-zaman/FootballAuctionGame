/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */

public class Manager extends Person {

    private String teamName;

    public Manager(String name, String teamName) {
        super(name);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public void displayInfo() {
        System.out.println("Manager : " + getName());
        System.out.println("Team    : " + teamName);
    }
}
