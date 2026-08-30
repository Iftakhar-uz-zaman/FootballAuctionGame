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

public class PostAuctionFrame extends javax.swing.JFrame implements ServerConnection.GameStateListener {

    private ServerConnection connection;
    private String myTeamName;
    private Team myTeam;

    private JLabel infoLabel;
    private DefaultListModel<Player> squadModel = new DefaultListModel<>();
    private JList<Player> squadList = new JList<>(squadModel);
    private DefaultListModel<Player> availableModel = new DefaultListModel<>();
    private JList<Player> availableList = new JList<>(availableModel);
    private JLabel statusLabel = new JLabel(" ");

    private JComboBox<String> formationSelector;
    private JPanel pitchPanel;
    private ArrayList<JButton> slotButtons = new ArrayList<>();

    private static final java.awt.Color GREEN = new java.awt.Color(0, 153, 0);
    private static final java.awt.Color PITCH_GREEN = new java.awt.Color(30, 130, 76);
    private static final java.awt.Color PITCH_LINE = new java.awt.Color(255, 255, 255, 120);

    public PostAuctionFrame(ServerConnection connection, String myTeamName, GameStateUpdate initialState) {

        initComponents();

        this.connection = connection;
        this.myTeamName = myTeamName;

        setTitle("Manage Team — " + myTeamName);

        mainContainer.setLayout(new java.awt.BorderLayout());
        mainContainer.add(buildScreen(), java.awt.BorderLayout.CENTER);

        connection.addListener(this);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                connection.removeListener(PostAuctionFrame.this);
            }
        });

        applyState(initialState);
    }

    private JPanel buildScreen() {

        JPanel outer = new JPanel(new java.awt.BorderLayout(12, 12));
        outer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("Manage Team", SwingConstants.CENTER);
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        header.setForeground(GREEN);
        outer.add(header, java.awt.BorderLayout.NORTH);

        infoLabel = new JLabel();
        infoLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        infoLabel.setOpaque(true);
        infoLabel.setBackground(new java.awt.Color(235, 245, 235));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel centerSplit = new JPanel(new java.awt.GridLayout(1, 2, 12, 0));
        centerSplit.add(buildSquadPanel());
        centerSplit.add(buildPitchPanel());

        JPanel centerWrap = new JPanel(new java.awt.BorderLayout(0, 12));
        centerWrap.add(infoLabel, java.awt.BorderLayout.NORTH);
        centerWrap.add(centerSplit, java.awt.BorderLayout.CENTER);
        outer.add(centerWrap, java.awt.BorderLayout.CENTER);

        outer.add(buildControls(outer), java.awt.BorderLayout.SOUTH);

        return outer;
    }

    private JPanel buildSquadPanel() {

        JPanel panel = new JPanel(new java.awt.GridLayout(2, 1, 0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        squadList.setCellRenderer(new PlayerNameRenderer());
        squadList.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        JScrollPane squadScroll = new JScrollPane(squadList);
        squadScroll.setBorder(coloredTitledBorder("Your Squad", GREEN));

        availableList.setCellRenderer(new PlayerNameRenderer());
        availableList.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        JScrollPane availableScroll = new JScrollPane(availableList);
        availableScroll.setBorder(coloredTitledBorder("Available Players (base price)", new java.awt.Color(0, 90, 180)));

        panel.add(squadScroll);
        panel.add(availableScroll);

        return panel;
    }

    private JPanel buildPitchPanel() {

        JPanel wrapper = new JPanel(new java.awt.BorderLayout(0, 8));
        wrapper.setBorder(coloredTitledBorder("Starting Lineup", GREEN));

        JPanel formationRow = new JPanel();
        formationRow.add(new JLabel("Formation:"));

        formationSelector = new JComboBox<>(Team.getFormationOptions().keySet().toArray(new String[0]));
        formationSelector.addActionListener(e -> {

            String chosen = (String) formationSelector.getSelectedItem();

            if (myTeam == null || !chosen.equals(myTeam.getFormation())) {
                connection.sendSetFormation(chosen);
            }
        });
        formationRow.add(formationSelector);

        wrapper.add(formationRow, java.awt.BorderLayout.NORTH);
        pitchPanel = new JPanel(null) {
    @Override
    protected void paintComponent(java.awt.Graphics g) {

        super.paintComponent(g);
        g.setColor(PITCH_GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(PITCH_LINE);
        g.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
        g.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2);
        g.drawOval(getWidth() / 2 - 40, getHeight() / 2 - 40, 80, 80);
    }
};
pitchPanel.setPreferredSize(new java.awt.Dimension(460, 360));

pitchPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
    @Override
    public void componentResized(java.awt.event.ComponentEvent e) {
        if (myTeam != null && myTeam.getFormation() != null) {
            rebuildPitch();
        }
    }
});

wrapper.add(pitchPanel, java.awt.BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel buildControls(JPanel parentForDialogs) {

        JButton dropButton = styledButton("Drop Selected (50% refund)", new java.awt.Color(180, 40, 40));
        JButton buyButton = styledButton("Buy Selected (base price)", GREEN);
        JButton downloadButton = styledButton("Download Team Sheet", new java.awt.Color(0, 90, 180));
        JButton logoutButton = styledButton("Logout", java.awt.Color.DARK_GRAY);

        JPanel controls = new JPanel();
        controls.add(dropButton);
        controls.add(buyButton);
        controls.add(downloadButton);
        controls.add(logoutButton);

        JPanel south = new JPanel(new java.awt.BorderLayout());
        south.add(controls, java.awt.BorderLayout.NORTH);

        statusLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        south.add(statusLabel, java.awt.BorderLayout.SOUTH);

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

            int result = chooser.showSaveDialog(parentForDialogs);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    TeamSheetExporter.exportTeamSheet(myTeam, chooser.getSelectedFile());
                    statusLabel.setText("Team sheet saved.");
                } catch (IOException ex) {
                    statusLabel.setText("Error saving file: " + ex.getMessage());
                }
            }
        });

        logoutButton.addActionListener(e -> {
            connection.removeListener(this);
            dispose();
        });

        return south;
    }

    private JButton styledButton(String text, java.awt.Color color) {

        JButton button = new JButton(text);
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        button.setForeground(java.awt.Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setPreferredSize(new java.awt.Dimension(190, 36));

        return button;
    }

    private javax.swing.border.TitledBorder coloredTitledBorder(String title, java.awt.Color color) {

        javax.swing.border.TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(color);
        border.setTitleFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        return border;
    }

    private void rebuildPitch() {

    pitchPanel.removeAll();
    slotButtons.clear();

    if (myTeam == null || myTeam.getFormation() == null) {
        pitchPanel.revalidate();
        pitchPanel.repaint();
        return;
    }

    int pitchW = pitchPanel.getWidth() > 0 ? pitchPanel.getWidth() : 460;
    int pitchH = pitchPanel.getHeight() > 0 ? pitchPanel.getHeight() : 360;

    int boxW = Math.max(75, pitchW / 6);
    int boxH = Math.max(40, pitchH / 9);

    Team.FormationSlot[] slots = Team.getFormationOptions().get(myTeam.getFormation());

    for (int i = 0; i < slots.length; i++) {

        int slotIndex = i;
        Team.FormationSlot slot = slots[i];

        JButton box = new JButton();
        box.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        box.setFocusPainted(false);

        int x = (slot.xPercent * pitchW / 100) - (boxW / 2);
        int y = (slot.yPercent * pitchH / 100) - (boxH / 2);
        box.setBounds(x, y, boxW, boxH);

        box.addActionListener(e -> openSlotPicker(slotIndex, slot.role));

        pitchPanel.add(box);
        slotButtons.add(box);
    }

    refreshPitchLabels();

    pitchPanel.revalidate();
    pitchPanel.repaint();
}

    private void refreshPitchLabels() {

        if (myTeam == null || myTeam.getFormation() == null) {
            return;
        }

        Team.FormationSlot[] slots = Team.getFormationOptions().get(myTeam.getFormation());
        ArrayList<Player> lineup = myTeam.getLineup();

        for (int i = 0; i < slotButtons.size() && i < slots.length; i++) {

            JButton box = slotButtons.get(i);
            Player assigned = i < lineup.size() ? lineup.get(i) : null;

            if (assigned == null) {

                box.setText("<html><center><b>" + slots[i].role + "</b><br>+ Add</center></html>");
                box.setBackground(java.awt.Color.WHITE);
                box.setForeground(java.awt.Color.DARK_GRAY);

            } else {

                box.setText("<html><center><b>" + slots[i].role + "</b><br>" + assigned.getName() + "</center></html>");
                box.setBackground(new java.awt.Color(255, 205, 0));
                box.setForeground(java.awt.Color.BLACK);
            }
        }
    }

    private void openSlotPicker(int slotIndex, String role) {

        if (myTeam == null) {
            return;
        }

        JComboBox<Player> picker = new JComboBox<>();
        picker.setRenderer(new PlayerNameRenderer());
        picker.addItem(null);

        for (Player p : myTeam.getSquad()) {
            picker.addItem(p);
        }

        ArrayList<Player> lineup = myTeam.getLineup();

        if (slotIndex < lineup.size()) {
            picker.setSelectedItem(lineup.get(slotIndex));
        }

        int result = JOptionPane.showConfirmDialog(
                this, picker, "Assign " + role,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            Player selected = (Player) picker.getSelectedItem();
            connection.sendSetLineupSlot(slotIndex, selected == null ? null : selected.getId());
        }
    }

    private void applyState(GameStateUpdate update) {

        String previousFormation = myTeam == null ? null : myTeam.getFormation();

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

        if (myTeam.getFormation() != null) {
            formationSelector.setSelectedItem(myTeam.getFormation());
        }

        boolean formationChanged = myTeam.getFormation() != null
                && !myTeam.getFormation().equals(previousFormation);

        if (formationChanged || slotButtons.isEmpty()) {
            rebuildPitch();
        } else {
            refreshPitchLabels();
        }
    }

    @Override
    public void onStateUpdate(GameStateUpdate update) {
        SwingUtilities.invokeLater(() -> applyState(update));
    }

    @Override
    public void onResponse(ServerResponse response) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(response.getStatusMessage()));
    }

    @Override
    public void onDisconnected(String reason) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(reason));
    }

    private static class PlayerNameRenderer extends DefaultListCellRenderer {

        @Override
        public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setText(value == null ? "-- Empty --" : ((Player) value).getName());

            return this;
        }
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