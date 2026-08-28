/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// ServerResponse.java
public class ServerResponse extends Message {

    private boolean success;
    private String statusMessage;

    public ServerResponse(boolean success, String statusMessage) {
        this.success = success;
        this.statusMessage = statusMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public String describe() {
        return "Server response: " + statusMessage;
    }
}
