/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// SetFormationRequest.java
public class SetFormationRequest extends Message {

    private String formation;

    public SetFormationRequest(String formation) {
        this.formation = formation;
    }

    public String getFormation() {
        return formation;
    }

    @Override
    public String describe() {
        return "Set formation: " + formation;
    }
}
