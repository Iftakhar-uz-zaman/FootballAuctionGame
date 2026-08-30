/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// SetLineupSlotRequest.java
public class SetLineupSlotRequest extends Message {

    private int slotIndex;
    private Integer playerId; // null means clear this slot

    public SetLineupSlotRequest(int slotIndex, Integer playerId) {
        this.slotIndex = slotIndex;
        this.playerId = playerId;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    @Override
    public String describe() {
        return "Set lineup slot " + slotIndex;
    }
}