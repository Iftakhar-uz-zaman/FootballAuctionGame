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
    private String password;

// existing constructor still works, defaults to no password
public Manager(String name, String teamName) {
    this(name, teamName, null);
}

// NEW overloaded constructor — this is method/constructor overloading, same concept as your Bid/Player overloads
public Manager(String name, String teamName, String password) {
    super(name);
    this.teamName = teamName;
    this.password = password;
}

public boolean checkPassword(String attempt) {
    return password != null && password.equals(attempt);
}

public void setPassword(String password) {
    this.password = password;
}

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String getRole() {
        return "Manager";
    }

    @Override
    public String toString() {
        return getName() + " (" + teamName + ")";
    }
}