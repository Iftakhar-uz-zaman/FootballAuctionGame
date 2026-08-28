/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
public class FootballAuctionGame {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {

            Object[] options = {"Host a new auction", "Join an existing auction"};

            int choice = javax.swing.JOptionPane.showOptionDialog(
                    null,
                    "Are you hosting or joining?",
                    "Football Auction Game",
                    javax.swing.JOptionPane.DEFAULT_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {

                SetupFrame frame = new SetupFrame();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            } else {

                JoinFrame frame = new JoinFrame();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
