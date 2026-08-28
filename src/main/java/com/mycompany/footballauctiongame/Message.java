/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
import java.io.Serializable;

public abstract class Message implements Serializable{
    private static final long serialVersionUID = 1L;
    public abstract String describe();
}
