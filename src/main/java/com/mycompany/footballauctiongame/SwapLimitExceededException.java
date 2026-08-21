/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
//a team can only add same number of players they have dropped - otherwise it will throw this exception
public class SwapLimitExceededException extends AuctionException {
    public SwapLimitExceededException(String message) {
        super(message);
    }
}
