/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
// SoundManager.java
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {

    public static void play(String fileName) {

        try {
            File file = new File("sounds/" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio format: " + fileName);
        } catch (LineUnavailableException e) {
            System.out.println("Audio output unavailable.");
        } catch (IOException e) {
            System.out.println("Could not read sound file: " + fileName);
        }
    }
}
