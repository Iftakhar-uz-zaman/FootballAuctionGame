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
import java.util.ArrayList;
public class PostAuctionFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PostAuctionFrame.class.getName());
    private AuctionEngine engine;
private java.awt.CardLayout cardLayout;
private JPanel currentTeamPanel;

public PostAuctionFrame(AuctionEngine engine) {
    initComponents();
    this.engine = engine;

    setTitle("Post-Auction — Manage Teams");

    cardLayout = new java.awt.CardLayout();
    mainContainer.setLayout(cardLayout);

    mainContainer.add(buildLoginPanel(), "login");

    cardLayout.show(mainContainer, "login");
}
    /**
     * Creates new form PostAuctionFrame
     */
    public PostAuctionFrame() {
        initComponents();
    }

    private JPanel buildLoginPanel() {

    JPanel panel = new JPanel();
    panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
    panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 60, 40, 60));

    JComboBox<Team> teamSelector = new JComboBox<>();
    for (Team team : engine.getTeams()) {
        teamSelector.addItem(team);
    }
    teamSelector.setRenderer((list, value, index, isSelected, hasFocus)
            -> new JLabel(value.getTeamName()));

    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JLabel statusLabel = new JLabel(" ");

    panel.add(new JLabel("Select your team:"));
    panel.add(teamSelector);
    panel.add(javax.swing.Box.createVerticalStrut(10));
    panel.add(new JLabel("Password:"));
    panel.add(passwordField);
    panel.add(javax.swing.Box.createVerticalStrut(10));
    panel.add(loginButton);
    panel.add(statusLabel);

panel.add(javax.swing.Box.createVerticalStrut(20));   // NEW

JButton leagueButton = new JButton("Run League Simulation");   // NEW
panel.add(leagueButton);                                        // NEW

leagueButton.addActionListener(e -> {                            // NEW

    String results = engine.simulateLeague();

    JTextArea resultArea = new JTextArea(results);
    resultArea.setEditable(false);
    resultArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(resultArea);
    scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

    JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "League Results",
            JOptionPane.INFORMATION_MESSAGE
    );
});
    loginButton.addActionListener(e -> {

        Team selectedTeam = (Team) teamSelector.getSelectedItem();
        String attempt = new String(passwordField.getPassword());

        if (selectedTeam == null) {
            statusLabel.setText("No teams available.");
            return;
        }

        if (!selectedTeam.getManager().checkPassword(attempt)) {
            statusLabel.setText("Incorrect password.");
            passwordField.setText("");
            return;
        }

        if (currentTeamPanel != null) {
            mainContainer.remove(currentTeamPanel);
        }

        currentTeamPanel = buildTeamPanel(selectedTeam);
        mainContainer.add(currentTeamPanel, "team");
        cardLayout.show(mainContainer, "team");

        passwordField.setText("");
        statusLabel.setText(" ");
    });

    return panel;
}
    private JPanel buildTeamPanel(Team team) {

    JPanel panel = new JPanel(new java.awt.BorderLayout(10, 10));
    panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JLabel infoLabel = new JLabel();
    infoLabel.setFont(infoLabel.getFont().deriveFont(java.awt.Font.BOLD, 14f));
    panel.add(infoLabel, java.awt.BorderLayout.NORTH);

    DefaultListModel<Player> squadModel = new DefaultListModel<>();
    JList<Player> squadList = new JList<>(squadModel);
    JScrollPane squadScroll = new JScrollPane(squadList);
    squadScroll.setBorder(BorderFactory.createTitledBorder("Squad"));

    DefaultListModel<Player> availableModel = new DefaultListModel<>();
    JList<Player> availableList = new JList<>(availableModel);
    JScrollPane availableScroll = new JScrollPane(availableList);
    availableScroll.setBorder(BorderFactory.createTitledBorder("Available Players (buy at base price)"));

    JPanel listsPanel = new JPanel(new java.awt.GridLayout(1, 2, 10, 10));
    listsPanel.add(squadScroll);
    listsPanel.add(availableScroll);
    panel.add(listsPanel, java.awt.BorderLayout.CENTER);

    JButton dropButton = new JButton("Drop Selected (50% refund)");
    JButton buyButton = new JButton("Buy Selected (base price)");
    JButton downloadButton = new JButton("Download Team Sheet");
    JButton logoutButton = new JButton("Logout");
    JLabel statusLabel = new JLabel(" ");

    JPanel controls = new JPanel();
    controls.add(dropButton);
    controls.add(buyButton);
    controls.add(downloadButton);
    controls.add(logoutButton);

    JPanel south = new JPanel(new java.awt.BorderLayout());
    south.add(controls, java.awt.BorderLayout.NORTH);
    south.add(statusLabel, java.awt.BorderLayout.SOUTH);
    panel.add(south, java.awt.BorderLayout.SOUTH);

    Runnable refresh = () -> {

        infoLabel.setText(
                team.getTeamName()
                + "   |   Manager: " + team.getManager().getName()
                + "   |   Purse: " + team.getPurse()
                + "   |   Chemistry: " + team.getChemistry()
                + "   |   Fan Happiness: " + team.getFanHappiness()
                + "   |   Dropped: " + team.getDroppedCount()
                + "   |   Added: " + team.getAddedCount()
        );

        squadModel.clear();
        for (Player p : team.getSquad()) {
            squadModel.addElement(p);
        }

        availableModel.clear();
        for (Player p : engine.getAvailablePlayers()) {
            availableModel.addElement(p);
        }
    };

    refresh.run();

    dropButton.addActionListener(e -> {

        Player selected = squadList.getSelectedValue();

        if (selected == null) {
            statusLabel.setText("Select a player from your squad first.");
            return;
        }

        try {
            engine.dropPlayerFromTeam(team, selected);
            statusLabel.setText(selected.getName() + " dropped. Refund added to purse.");
        } catch (PlayerNotFoundException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
        }

        refresh.run();
    });

    buyButton.addActionListener(e -> {

        Player selected = availableList.getSelectedValue();

        if (selected == null) {
            statusLabel.setText("Select an available player first.");
            return;
        }

        try {
            engine.buyAvailablePlayer(team, selected);
            statusLabel.setText(selected.getName() + " added at base price "
                    + selected.getBasePrice() + ".");
        } catch (SwapLimitExceededException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
        } catch (InsufficientPurseException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
        } catch (PlayerNotFoundException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
        }

        refresh.run();
    });

    downloadButton.addActionListener(e -> {

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(team.getTeamName() + "_sheet.txt"));

        int result = chooser.showSaveDialog(panel);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                TeamSheetExporter.exportTeamSheet(team, chooser.getSelectedFile());
                statusLabel.setText("Team sheet saved.");
            } catch (IOException ex) {
                statusLabel.setText("Error saving file: " + ex.getMessage());
            }
        }
    });

    logoutButton.addActionListener(e -> {
        mainContainer.remove(panel);
        currentTeamPanel = null;
        cardLayout.show(mainContainer, "login");
    });

    return panel;
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

    /**
     * @param args the command line arguments
     */
    /*
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(() -> new PostAuctionFrame().setVisible(true));
    }
*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainContainer;
    // End of variables declaration//GEN-END:variables
}
