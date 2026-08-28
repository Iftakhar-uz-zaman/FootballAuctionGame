/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.footballauctiongame;

/**
 *
 * @author Lenovo
 */
import javax.swing.*;
import java.io.IOException;
import java.awt.*;
import java.io.File;

//the third screen where post auction activities are done
public class PostAuctionFrame extends javax.swing.JFrame implements ServerConnection.GameStateListener{
    private ServerConnection connection;
    private String myTeamName;
    private Team myTeam;

    private JLabel infoLabel;
    private DefaultListModel<Player> squadModel = new DefaultListModel<>();
    private JList<Player> squadList = new JList<>(squadModel);
    private DefaultListModel<Player> availableModel = new DefaultListModel<>();
    private JList<Player> availableList = new JList<>(availableModel);
    private JLabel statusLabel = new JLabel(" ");
    
    //creates card layout and login screen
    public PostAuctionFrame(ServerConnection connection, String myTeamName, GameStateUpdate initialState) {
        initComponents();
        this.connection = connection;
        this.myTeamName = myTeamName;

        setTitle("Manage Team — " + myTeamName);

        mainContainer.setLayout(new java.awt.BorderLayout(10, 10));
        mainContainer.add(buildPanel(), java.awt.BorderLayout.CENTER);

        connection.addListener(this);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                connection.removeListener(PostAuctionFrame.this);
            }
        });
        applyState(initialState);
    }
    
    public PostAuctionFrame() {
        initComponents();
    }

    private JPanel buildPanel() {

        JPanel panel = new JPanel(new java.awt.BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        infoLabel = new JLabel();
        infoLabel.setFont(infoLabel.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        panel.add(infoLabel, java.awt.BorderLayout.NORTH);

        JScrollPane squadScroll = new JScrollPane(squadList);
        squadScroll.setBorder(BorderFactory.createTitledBorder("Squad"));

        JScrollPane availableScroll = new JScrollPane(availableList);
        availableScroll.setBorder(BorderFactory.createTitledBorder("Available Players (base price)"));
        
        JPanel listsPanel = new JPanel(new java.awt.GridLayout(1, 2, 10, 10));
        listsPanel.add(squadScroll);
        listsPanel.add(availableScroll);
        panel.add(listsPanel, java.awt.BorderLayout.CENTER);

        JButton dropButton = new JButton("Drop Selected (50% refund)");
        JButton buyButton = new JButton("Buy Selected (base price)");
        JButton downloadButton = new JButton("Download Team Sheet");
        JButton leagueButton = new JButton("Run League Simulation");

        JPanel controls = new JPanel();
        controls.add(dropButton);
        controls.add(buyButton);
        controls.add(downloadButton);
        controls.add(leagueButton);
        
        JPanel south = new JPanel(new java.awt.BorderLayout());
        south.add(controls, java.awt.BorderLayout.NORTH);
        south.add(statusLabel, java.awt.BorderLayout.SOUTH);
        panel.add(south, java.awt.BorderLayout.SOUTH);

        dropButton.addActionListener(e -> {

            Player selected = squadList.getSelectedValue();

            if (selected == null) {
                statusLabel.setText("Select a player from your squad first.");
                return;
            }

            connection.sendDropPlayer(selected.getId());
        });
        
        buyButton.addActionListener(e -> {

            Player selected = availableList.getSelectedValue();

            if (selected == null) {
                statusLabel.setText("Select an available player first.");
                return;
            }

            connection.sendBuyPlayer(selected.getId());
        });
        
        downloadButton.addActionListener(e -> {

            if (myTeam == null) {
                statusLabel.setText("No team data yet.");
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File(myTeam.getTeamName() + "_sheet.txt"));

            int result = chooser.showSaveDialog(panel);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    TeamSheetExporter.exportTeamSheet(myTeam, chooser.getSelectedFile());
                    statusLabel.setText("Team sheet saved.");
                } catch (IOException ex) {
                    statusLabel.setText("Error saving file: " + ex.getMessage());
                }
                }
        });

        leagueButton.addActionListener(e -> connection.sendRunLeague());

        return panel;
    }
    
    private void applyState(GameStateUpdate update) {

        for (Team team : update.getTeams()) {
            if (team.getTeamName().equals(myTeamName)) {
                myTeam = team;
                break;
            }
        }

        if (myTeam == null) {
            statusLabel.setText("Could not find your team in server data.");
            return;
        }
        
        infoLabel.setText(
                myTeam.getTeamName()
                + "   |   Manager: " + myTeam.getManager().getName()
                + "   |   Purse: " + myTeam.getPurse()
                + "   |   Chemistry: " + myTeam.getChemistry()
                + "   |   Fan Happiness: " + myTeam.getFanHappiness()
                + "   |   Dropped: " + myTeam.getDroppedCount()
                + "   |   Added: " + myTeam.getAddedCount()
        );

        squadModel.clear();
        for (Player p : myTeam.getSquad()) {
            squadModel.addElement(p);
        }
        availableModel.clear();
        for (Player p : update.getAvailablePlayers()) {
            availableModel.addElement(p);
        }
    }

    @Override
    public void onStateUpdate(GameStateUpdate update) {
        javax.swing.SwingUtilities.invokeLater(() -> applyState(update));
    }

    @Override
    public void onResponse(ServerResponse response) {
        javax.swing.SwingUtilities.invokeLater(() -> statusLabel.setText(response.getStatusMessage()));
    }
    public void onDisconnected(String reason) {
        javax.swing.SwingUtilities.invokeLater(() -> statusLabel.setText(reason));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        mainContainerLayout.setVerticalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainContainer;
    // End of variables declaration//GEN-END:variables
}
