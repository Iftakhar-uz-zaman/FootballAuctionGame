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
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;

//the second screen where it shows the ongoing ayction
public class MainFrame extends javax.swing.JFrame implements ServerConnection.GameStateListener{
    private ServerConnection connection;
    private String myTeamName;
    private GameStateUpdate latestUpdate;
    private double lastBidAmount = -1;
    private int lastPassedCount = -1;
    private Player lastPlayer = null;
    private String lastBidderName = "None";
    private Integer lastRevealedPlayerId = null;
    private int lastResolvedCountSeen = 0;
private javax.swing.Timer revealTimer;
private boolean isRevealing = false;

private javax.swing.Timer pulseTimer;
private boolean pulseGrowing = true;

public MainFrame(ServerConnection connection, String myTeamName) {

    initComponents();
    buildCustomLayout();

    this.connection = connection;
    this.myTeamName = myTeamName;

    btnBid.setEnabled(false);
    btnPass.setEnabled(false);
    btnPause.setEnabled(false);
    btnManageTeams.setEnabled(false);

    connection.addListener(this);
}

private void buildCustomLayout() {

    getContentPane().removeAll();
    getContentPane().setLayout(new java.awt.BorderLayout(15, 15));
    ((javax.swing.JComponent) getContentPane()).setBorder(
            javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JLabel header = new JLabel("⚽ Football Auction Game", javax.swing.SwingConstants.CENTER);
    header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
    getContentPane().add(header, java.awt.BorderLayout.NORTH);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new javax.swing.BoxLayout(centerPanel, javax.swing.BoxLayout.Y_AXIS));

    JPanel playerCard = new JPanel(new java.awt.GridLayout(0, 2, 12, 12));
    playerCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 2, true),
            javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));

    lblPlayerName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
    lblPlayerName.setForeground(new java.awt.Color(0, 102, 51));

    lblPosition.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
    lblOverall.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
    lblBasePrice.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));

    lblCurrentBid.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

    lblHighestBidder.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
    lblHighestBidder.setForeground(new java.awt.Color(204, 0, 0));

    lblCurrentTurn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
    lblCurrentTurn.setForeground(java.awt.Color.RED);
    lblCurrentTurn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    playerCard.add(makeCaption("Player:"));
    playerCard.add(lblPlayerName);
    playerCard.add(makeCaption("Position:"));
    playerCard.add(lblPosition);
    playerCard.add(makeCaption("Overall:"));
    playerCard.add(lblOverall);
    playerCard.add(makeCaption("Base Price:"));
    playerCard.add(lblBasePrice);
    playerCard.add(makeCaption("Current Bid:"));
    playerCard.add(lblCurrentBid);
    playerCard.add(makeCaption("Highest Bidder:"));
    playerCard.add(lblHighestBidder);
    playerCard.add(makeCaption("Time Left:"));
    playerCard.add(lblCurrentTurn);

    centerPanel.add(playerCard);
    centerPanel.add(javax.swing.Box.createVerticalStrut(15));

    jLabel16.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel16.setOpaque(true);
    jLabel16.setBackground(new java.awt.Color(230, 230, 230));
    jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    centerPanel.add(jLabel16);
    centerPanel.add(javax.swing.Box.createVerticalStrut(15));

    JPanel controls = new JPanel();
    btnBid.setPreferredSize(new java.awt.Dimension(140, 40));
    btnBid.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    btnPass.setPreferredSize(new java.awt.Dimension(140, 40));
    btnPass.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    btnPause.setPreferredSize(new java.awt.Dimension(140, 40));
    btnPause.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    btnManageTeams.setPreferredSize(new java.awt.Dimension(170, 40));
    btnManageTeams.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    controls.add(btnBid);
    controls.add(btnPass);
    controls.add(btnPause);
    controls.add(btnManageTeams);
    centerPanel.add(controls);

    getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

    jScrollPane2.setViewportView(teamsPanel);
    jScrollPane2.setPreferredSize(new java.awt.Dimension(340, 0));
    jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Teams"));
    getContentPane().add(jScrollPane2, java.awt.BorderLayout.EAST);

    getContentPane().revalidate();
    getContentPane().repaint();
}

private JLabel makeCaption(String text) {
    JLabel label = new JLabel(text);
    label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
    return label;
}
    
    public void onStateUpdate(GameStateUpdate update) {
        javax.swing.SwingUtilities.invokeLater(() -> refreshScreen(update));
    }
    
    @Override
public void onResponse(ServerResponse response) {

    javax.swing.SwingUtilities.invokeLater(() -> {

        if (!response.isSuccess()) {
            showBanner(response.getStatusMessage(), new java.awt.Color(153, 0, 0));
        }
    });
}

    public void onDisconnected(String reason) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            jLabel16.setText(reason);
            btnBid.setEnabled(false);
        });
    }
    
    private void refreshScreen(GameStateUpdate update) {

    latestUpdate = update;

    Player newPlayer = update.getCurrentPlayer();
    boolean playerChanged = lastPlayer != null
            && (newPlayer == null || newPlayer.getId() != lastPlayer.getId());

        if (update.getResolvedCount() > lastResolvedCountSeen) {

    lastResolvedCountSeen = update.getResolvedCount();

    if (update.getLastResolvedWinnerName() != null) {

        SoundManager.play("sold.wav");
        showBanner(update.getLastResolvedPlayerName() + " SOLD to "
                + update.getLastResolvedWinnerName() + " for " + update.getLastResolvedPrice() + "!",
                new java.awt.Color(0, 153, 0));

    } else {

        SoundManager.play("unsold.wav");
        showBanner(update.getLastResolvedPlayerName() + " went UNSOLD!", new java.awt.Color(153, 0, 0));
    }

    } else if (update.getCurrentBidAmount() > lastBidAmount
        || (!update.getCurrentBidderName().equals(lastBidderName)
            && !update.getCurrentBidderName().equals("None"))) {

    SoundManager.play("bid.wav");
    showBanner(update.getCurrentBidderName() + " bid " + update.getCurrentBidAmount(),
            new java.awt.Color(0, 90, 180));

} else if (update.getPassedCount() > lastPassedCount) {

        showBanner(update.getLastPassingTeam() + " passed.", java.awt.Color.GRAY);
    }

    lastBidAmount = update.getCurrentBidAmount();
    lastPlayer = newPlayer;
    lastBidderName = update.getCurrentBidderName();
    lastPassedCount = update.getPassedCount();

    if (update.isAuctionFinished()) {
        lblPlayerName.setText("Auction Finished!");
        btnBid.setEnabled(false);
        btnPass.setEnabled(false);
        btnPause.setEnabled(false);
        btnManageTeams.setEnabled(true);

        updateTeams(update.getTeams(), update.getConnectedTeamNames());
        return;
    }

        Player player = update.getCurrentPlayer();

    boolean isNewPlayer = lastRevealedPlayerId == null || player.getId() != lastRevealedPlayerId;

    if (isNewPlayer) {

        lastRevealedPlayerId = player.getId();
        startReveal(update);

    } else if (!isRevealing) {

        displayPlayerStats(update);
    }

    updateCountdownDisplay(update);
    updateTeams(update.getTeams(), update.getConnectedTeamNames());
}
    
    private void startReveal(GameStateUpdate update) {

    isRevealing = true;

    if (revealTimer != null && revealTimer.isRunning()) {
        revealTimer.stop();
    }

    lblPlayerName.setText("🔍 Revealing next player...");
    lblPlayerName.setForeground(java.awt.Color.DARK_GRAY);
    lblPosition.setText("...");
    lblOverall.setText("...");
    lblBasePrice.setText("...");
    lblCurrentBid.setText("...");
    lblHighestBidder.setText("...");

    btnBid.setEnabled(false);
    btnPass.setEnabled(false);

    SoundManager.play("reveal.wav");

    revealTimer = new javax.swing.Timer(1200, e -> {
        isRevealing = false;
        displayPlayerStats(latestUpdate);
    });
    revealTimer.setRepeats(false);
    revealTimer.start();
}

private void displayPlayerStats(GameStateUpdate update) {

    Player player = update.getCurrentPlayer();

    if (player == null) {
        return;
    }

    lblPlayerName.setText(player.getName());
    lblPlayerName.setForeground(new java.awt.Color(0, 102, 51));
    lblPosition.setText(player.getPosition());
    lblOverall.setText(String.valueOf(player.getOverall()));
    lblBasePrice.setText(String.valueOf(player.getBasePrice()));
    lblCurrentBid.setText(String.valueOf(update.getCurrentBidAmount()));
    lblHighestBidder.setText(update.getCurrentBidderName());

    updateActionButtons(update);
}

private void updateActionButtons(GameStateUpdate update) {

    boolean isHighestBidder = myTeamName.equals(update.getCurrentBidderName());
    boolean canAct = update.isAllTeamsConnected() && !update.isPaused() && !isRevealing;

    btnBid.setEnabled(canAct && !isHighestBidder);
    btnPass.setEnabled(canAct && !isHighestBidder);
    btnPause.setEnabled(update.isAllTeamsConnected());
}

private void updateCountdownDisplay(GameStateUpdate update) {

    if (!update.isAllTeamsConnected()) {

        stopPulse();
        lblCurrentTurn.setText("WAITING");
        lblCurrentTurn.setForeground(java.awt.Color.GRAY);

        jLabel16.setText("Waiting for " + update.getConnectedTeamNames().size() + "/"
                + update.getTeams().size() + " managers to connect...");
        jLabel16.setForeground(java.awt.Color.BLACK);

    } else if (update.isPaused()) {

        stopPulse();
        lblCurrentTurn.setText("PAUSED");
        lblCurrentTurn.setForeground(java.awt.Color.ORANGE);

    } else {

        int seconds = update.getSecondsRemaining();
        lblCurrentTurn.setText(seconds + "s");

        if (seconds <= 5 && seconds > 0 && !isRevealing) {
            startPulse();
        } else {
            stopPulse();
            lblCurrentTurn.setForeground(java.awt.Color.RED);
        }
    }
}

private void startPulse() {

    if (pulseTimer != null && pulseTimer.isRunning()) {
        return;
    }

    pulseTimer = new javax.swing.Timer(400, e -> {

        pulseGrowing = !pulseGrowing;

        lblCurrentTurn.setForeground(pulseGrowing
                ? java.awt.Color.RED
                : new java.awt.Color(120, 0, 0));
    });
    pulseTimer.start();
}

private void stopPulse() {

    if (pulseTimer != null) {
        pulseTimer.stop();
    }

    lblCurrentTurn.setForeground(java.awt.Color.RED);
}
    
    private void showBanner(String text, java.awt.Color color) {

    jLabel16.setText(text);
    jLabel16.setForeground(color);

    javax.swing.Timer clearTimer = new javax.swing.Timer(4000, e -> {
        jLabel16.setText("Ready");
        jLabel16.setForeground(java.awt.Color.BLACK);
    });
    clearTimer.setRepeats(false);
    clearTimer.start();
}
    
        private void updateTeams(java.util.ArrayList<Team> teams, java.util.ArrayList<String> connectedTeamNames) {

    teamsPanel.removeAll();
    teamsPanel.setLayout(new javax.swing.BoxLayout(teamsPanel, javax.swing.BoxLayout.Y_AXIS));

    for (Team team : teams) {

        boolean isConnected = connectedTeamNames.contains(team.getTeamName());

        JPanel card = new JPanel(new java.awt.BorderLayout(5, 2));
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5),
                javax.swing.BorderFactory.createTitledBorder(team.getTeamName())));

        JLabel statusBadge = new JLabel(isConnected ? "● Connected" : "○ Waiting to join");
        statusBadge.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        statusBadge.setForeground(isConnected
                ? new java.awt.Color(0, 153, 0)
                : java.awt.Color.GRAY);

        JLabel managerLabel = new JLabel("Manager: " + team.getManager().getName());

        JPanel northPanel = new JPanel(new java.awt.GridLayout(2, 1));
        northPanel.add(statusBadge);
        northPanel.add(managerLabel);
        card.add(northPanel, java.awt.BorderLayout.NORTH);

        double startingPurse = team.getPurse() + team.getTotalSquadValue();
        int percentLeft = startingPurse == 0 ? 0
                : (int) ((team.getPurse() / startingPurse) * 100);

        javax.swing.JProgressBar purseBar = new javax.swing.JProgressBar(0, 100);
        purseBar.setValue(percentLeft);
        purseBar.setStringPainted(true);
        purseBar.setString("Purse: " + team.getPurse());

        if (percentLeft < 20) {
            purseBar.setForeground(java.awt.Color.RED);
        } else if (percentLeft < 50) {
            purseBar.setForeground(java.awt.Color.ORANGE);
        } else {
            purseBar.setForeground(new java.awt.Color(0, 153, 0));
        }

        card.add(purseBar, java.awt.BorderLayout.CENTER);

        JLabel squadLabel = new JLabel(
                "<html>" + team.getPlayerNames().replace("\n", "<br>") + "</html>");

        JPanel southPanel = new JPanel(new java.awt.GridLayout(2, 1));
        southPanel.add(squadLabel);
        card.add(southPanel, java.awt.BorderLayout.SOUTH);

        teamsPanel.add(card);
        teamsPanel.add(javax.swing.Box.createVerticalStrut(8));
    }

    teamsPanel.revalidate();
    teamsPanel.repaint();
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblPosition = new javax.swing.JLabel();
        lblOverall = new javax.swing.JLabel();
        lblBasePrice = new javax.swing.JLabel();
        lblCurrentBid = new javax.swing.JLabel();
        lblHighestBidder = new javax.swing.JLabel();
        lblCurrentTurn = new javax.swing.JLabel();
        lblPlayerName = new javax.swing.JLabel();
        btnBid = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnManageTeams = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        teamsPanel = new javax.swing.JPanel();
        btnPass = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Football Auction Game");
        setSize(new java.awt.Dimension(900, 650));

        jLabel1.setText("Player Name:");

        jLabel2.setText("Position:");

        jLabel3.setText("Overall:");

        jLabel4.setText("Base Price:");

        jLabel5.setText("Current Bid:");

        jLabel6.setText("Highest Bidder:");

        jLabel7.setText("Current Turn:");

        lblPosition.setDisplayedMnemonic('l');
        lblPosition.setText("-");
        lblPosition.setName("lblPosition"); // NOI18N

        lblOverall.setText("-");
        lblOverall.setName("lblOverall"); // NOI18N

        lblBasePrice.setText("-");
        lblBasePrice.setName("lblBasePrice"); // NOI18N

        lblCurrentBid.setText("-");
        lblCurrentBid.setName("lblCurrentBid"); // NOI18N

        lblHighestBidder.setText("-");
        lblHighestBidder.setName("lblHighestBidder"); // NOI18N

        lblCurrentTurn.setText("-");
        lblCurrentTurn.setName("lblCurrentTurn"); // NOI18N

        lblPlayerName.setDisplayedMnemonic('l');
        lblPlayerName.setLabelFor(lblPlayerName);
        lblPlayerName.setText("-");
        lblPlayerName.setName("lblPlayerName"); // NOI18N

        btnBid.setBackground(new java.awt.Color(0, 153, 0));
        btnBid.setText("Bid");
        btnBid.setName("btnBid"); // NOI18N
        btnBid.addActionListener(this::btnBidActionPerformed);

        jLabel16.setText("Ready!!");
        jLabel16.setName("lblStatus"); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Football Auction Game");

        btnManageTeams.setText("Manage Teams");
        btnManageTeams.setEnabled(false);
        btnManageTeams.addActionListener(this::btnManageTeamsActionPerformed);

        javax.swing.GroupLayout teamsPanelLayout = new javax.swing.GroupLayout(teamsPanel);
        teamsPanel.setLayout(teamsPanelLayout);
        teamsPanelLayout.setHorizontalGroup(
            teamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 307, Short.MAX_VALUE)
        );
        teamsPanelLayout.setVerticalGroup(
            teamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(teamsPanel);

        btnPass.setBackground(new java.awt.Color(255, 51, 51));
        btnPass.setText("Pass");
        btnPass.addActionListener(this::btnPassActionPerformed);

        btnPause.setText("Pause");
        btnPause.addActionListener(this::btnPauseActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnManageTeams))
                            .addComponent(jLabel9))
                        .addGap(34, 34, 34))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel1))
                                        .addGap(77, 77, 77)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblCurrentTurn)
                                            .addComponent(lblHighestBidder)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblPlayerName)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel8))
                                            .addComponent(lblCurrentBid)
                                            .addComponent(lblBasePrice)
                                            .addComponent(lblOverall)
                                            .addComponent(lblPosition)))
                                    .addComponent(btnBid)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btnPass)
                                        .addGap(6, 6, 6))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnPause, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(jLabel16)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPause, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnManageTeams)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblPlayerName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(lblPosition))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lblOverall))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lblBasePrice))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(lblCurrentBid))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lblHighestBidder))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblCurrentTurn))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBid)
                            .addComponent(btnPass)))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //places bid
    private void btnBidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBidActionPerformed
        // TODO add your handling code here:
        connection.sendBid();
    }//GEN-LAST:event_btnBidActionPerformed

    //works when manage team button is pressed
    private void btnManageTeamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageTeamsActionPerformed
        // TODO add your handling code here:
    PostAuctionFrame frame = new PostAuctionFrame(connection, myTeamName, latestUpdate);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    }//GEN-LAST:event_btnManageTeamsActionPerformed

    private void btnPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPassActionPerformed
        // TODO add your handling code here:
        connection.sendPass();
    }//GEN-LAST:event_btnPassActionPerformed

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        // TODO add your handling code here:
        connection.sendTogglePause();
    }//GEN-LAST:event_btnPauseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBid;
    private javax.swing.JButton btnManageTeams;
    private javax.swing.JButton btnPass;
    private javax.swing.JButton btnPause;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBasePrice;
    private javax.swing.JLabel lblCurrentBid;
    private javax.swing.JLabel lblCurrentTurn;
    private javax.swing.JLabel lblHighestBidder;
    private javax.swing.JLabel lblOverall;
    private javax.swing.JLabel lblPlayerName;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JPanel teamsPanel;
    // End of variables declaration//GEN-END:variables
}
