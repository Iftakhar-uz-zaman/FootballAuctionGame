/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// LoginRequest.java
public class LoginRequest extends Message {

    private String teamName;
    private String password;

    public LoginRequest(String teamName, String password) {
        this.teamName = teamName;
        this.password = password;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String describe() {
        return "Login attempt for " + teamName;
    }
}
