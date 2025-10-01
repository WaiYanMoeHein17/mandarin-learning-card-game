package main_page;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import flashcards.FlashcardSelector;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import projects.SRSProgressTracker;

/**
 * This class adds progress tracking functionality to the MainPage.
 * It's a separate file that will be included in MainPage.java to add
 * the progress dashboard functionality without creating new dependencies.
 */
public class MainPageProgressDashboard {
    // Reference to the MainPage instance
    private MainPage mainPage;
    
    // Dashboard visibility flag
    private boolean dashboardVisible = false;
    
    // Dashboard UI components
    private JPanel progressPanel;
    private JProgressBar masteryProgressBar;
    private JLabel totalCardsValue;
    private JLabel reviewedCardsValue;
    private JLabel masteredCardsValue;
    private JProgressBar level0Bar, level1Bar, level2Bar, level3Bar, level4Bar, level5Bar;
    private JLabel masteryPercent;
    private JLabel dueCardCount;
    private JButton studyDueCardsButton;
    
    /**
     * Constructor that takes a reference to the MainPage
     */
    public MainPageProgressDashboard(MainPage mainPage) {
        this.mainPage = mainPage;
        initializeDashboardComponents();
    }
    
    /**
     * Initializes all the dashboard components
     */
    private void initializeDashboardComponents() {
        // Create the main progress panel
        progressPanel = new JPanel();
        progressPanel.setBackground(new Color(240, 240, 240));
        progressPanel.setLayout(new BorderLayout(10, 10));
        progressPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        progressPanel.setVisible(false);
        
        // Create the dashboard content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Title section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Progress Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalGlue());
        
        JButton backButton = new JButton("Back to Main");
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> hideProgressDashboard());
        titlePanel.add(backButton);
        
        contentPanel.add(titlePanel);
        
        // Statistics section
        JPanel statsPanel = createGradientPanel(new Color(52, 152, 219), new Color(41, 128, 185));
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Total cards stats
        JPanel totalCardsPanel = new JPanel();
        totalCardsPanel.setOpaque(false);
        totalCardsPanel.setLayout(new BoxLayout(totalCardsPanel, BoxLayout.Y_AXIS));
        JLabel totalCardsLabel = new JLabel("Total Cards");
        totalCardsLabel.setForeground(Color.WHITE);
        totalCardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalCardsValue = new JLabel("0");
        totalCardsValue.setForeground(Color.WHITE);
        totalCardsValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalCardsValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalCardsPanel.add(totalCardsLabel);
        totalCardsPanel.add(totalCardsValue);
        
        // Reviewed cards stats
        JPanel reviewedCardsPanel = new JPanel();
        reviewedCardsPanel.setOpaque(false);
        reviewedCardsPanel.setLayout(new BoxLayout(reviewedCardsPanel, BoxLayout.Y_AXIS));
        JLabel reviewedCardsLabel = new JLabel("Reviewed This Week");
        reviewedCardsLabel.setForeground(Color.WHITE);
        reviewedCardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reviewedCardsValue = new JLabel("0");
        reviewedCardsValue.setForeground(Color.WHITE);
        reviewedCardsValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        reviewedCardsValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        reviewedCardsPanel.add(reviewedCardsLabel);
        reviewedCardsPanel.add(reviewedCardsValue);
        
        // Mastered cards stats
        JPanel masteredCardsPanel = new JPanel();
        masteredCardsPanel.setOpaque(false);
        masteredCardsPanel.setLayout(new BoxLayout(masteredCardsPanel, BoxLayout.Y_AXIS));
        JLabel masteredCardsLabel = new JLabel("Mastered This Week");
        masteredCardsLabel.setForeground(Color.WHITE);
        masteredCardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        masteredCardsValue = new JLabel("0");
        masteredCardsValue.setForeground(Color.WHITE);
        masteredCardsValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        masteredCardsValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        masteredCardsPanel.add(masteredCardsLabel);
        masteredCardsPanel.add(masteredCardsValue);
        
        // Add stats panels with spacing
        statsPanel.add(Box.createHorizontalGlue());
        statsPanel.add(totalCardsPanel);
        statsPanel.add(Box.createHorizontalGlue());
        statsPanel.add(reviewedCardsPanel);
        statsPanel.add(Box.createHorizontalGlue());
        statsPanel.add(masteredCardsPanel);
        statsPanel.add(Box.createHorizontalGlue());
        
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Due today section
        JPanel duePanel = createGradientPanel(new Color(46, 204, 113), new Color(39, 174, 96));
        duePanel.setLayout(new BoxLayout(duePanel, BoxLayout.X_AXIS));
        duePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        duePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Due cards count
        JPanel dueCardsPanel = new JPanel();
        dueCardsPanel.setOpaque(false);
        dueCardsPanel.setLayout(new BoxLayout(dueCardsPanel, BoxLayout.Y_AXIS));
        JLabel dueCardsLabel = new JLabel("Due Today");
        dueCardsLabel.setForeground(Color.WHITE);
        dueCardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dueCardCount = new JLabel("0");
        dueCardCount.setForeground(Color.WHITE);
        dueCardCount.setFont(new Font("Segoe UI", Font.BOLD, 24));
        dueCardCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        dueCardsPanel.add(dueCardsLabel);
        dueCardsPanel.add(dueCardCount);
        
        // Mastery progress
        JPanel masteryPanel = new JPanel();
        masteryPanel.setOpaque(false);
        masteryPanel.setLayout(new BoxLayout(masteryPanel, BoxLayout.Y_AXIS));
        JLabel masteryLabel = new JLabel("Mastery Progress");
        masteryLabel.setForeground(Color.WHITE);
        masteryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel progressBarPanel = new JPanel();
        progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.X_AXIS));
        progressBarPanel.setOpaque(false);
        masteryProgressBar = new JProgressBar(0, 100);
        masteryProgressBar.setValue(0);
        masteryProgressBar.setStringPainted(false);
        masteryProgressBar.setPreferredSize(new Dimension(150, 20));
        masteryPercent = new JLabel("0%");
        masteryPercent.setForeground(Color.WHITE);
        masteryPercent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        progressBarPanel.add(masteryProgressBar);
        progressBarPanel.add(Box.createHorizontalStrut(10));
        progressBarPanel.add(masteryPercent);
        masteryPanel.add(masteryLabel);
        masteryPanel.add(Box.createVerticalStrut(5));
        masteryPanel.add(progressBarPanel);
        
        // Study button
        JPanel studyPanel = new JPanel();
        studyPanel.setOpaque(false);
        studyPanel.setLayout(new BoxLayout(studyPanel, BoxLayout.Y_AXIS));
        studyDueCardsButton = new JButton("Study Due Cards");
        studyDueCardsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        studyDueCardsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studyDueCardsButton.addActionListener(e -> studyDueCards());
        studyPanel.add(Box.createVerticalGlue());
        studyPanel.add(studyDueCardsButton);
        studyPanel.add(Box.createVerticalGlue());
        
        // Add due panel elements with spacing
        duePanel.add(Box.createHorizontalGlue());
        duePanel.add(dueCardsPanel);
        duePanel.add(Box.createHorizontalGlue());
        duePanel.add(masteryPanel);
        duePanel.add(Box.createHorizontalGlue());
        duePanel.add(studyPanel);
        duePanel.add(Box.createHorizontalGlue());
        
        contentPanel.add(duePanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // SRS levels section
        JPanel levelsPanel = new JPanel();
        levelsPanel.setLayout(new BoxLayout(levelsPanel, BoxLayout.Y_AXIS));
        levelsPanel.setOpaque(false);
        levelsPanel.setBorder(BorderFactory.createTitledBorder("SRS Level Distribution"));
        
        // Create level bars
        level0Bar = createLevelBar("New", new Color(192, 57, 43));
        level1Bar = createLevelBar("Level 1", new Color(230, 126, 34));
        level2Bar = createLevelBar("Level 2", new Color(241, 196, 15));
        level3Bar = createLevelBar("Level 3", new Color(46, 204, 113));
        level4Bar = createLevelBar("Level 4", new Color(52, 152, 219));
        level5Bar = createLevelBar("Level 5", new Color(155, 89, 182));
        
        levelsPanel.add(createLevelBarPanel("New", level0Bar));
        levelsPanel.add(Box.createVerticalStrut(5));
        levelsPanel.add(createLevelBarPanel("Level 1", level1Bar));
        levelsPanel.add(Box.createVerticalStrut(5));
        levelsPanel.add(createLevelBarPanel("Level 2", level2Bar));
        levelsPanel.add(Box.createVerticalStrut(5));
        levelsPanel.add(createLevelBarPanel("Level 3", level3Bar));
        levelsPanel.add(Box.createVerticalStrut(5));
        levelsPanel.add(createLevelBarPanel("Level 4", level4Bar));
        levelsPanel.add(Box.createVerticalStrut(5));
        levelsPanel.add(createLevelBarPanel("Level 5", level5Bar));
        
        contentPanel.add(levelsPanel);
        
        // Add scrolling support
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        
        progressPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add the panel to the MainPage
        mainPage.getContentPane().add(progressPanel);
    }
    
    /**
     * Creates a panel with a gradient background
     */
    private JPanel createGradientPanel(final Color color1, final Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
    }
    
    /**
     * Creates a styled progress bar for SRS levels
     */
    private JProgressBar createLevelBar(String level, Color color) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(false);
        bar.setBackground(new Color(240, 240, 240));
        bar.setForeground(color);
        bar.setToolTipText("0 cards");
        return bar;
    }
    
    /**
     * Creates a panel for displaying a level bar with label
     */
    private JPanel createLevelBarPanel(String levelName, JProgressBar bar) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(levelName);
        label.setPreferredSize(new Dimension(70, 20));
        
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(bar);
        
        return panel;
    }
    
    /**
     * Shows the progress dashboard with updated data
     */
    public void showProgressDashboard() {
        // Hide main content using reflection since the fields are not accessible directly
        hideMainPanels();
        
        // Show the progress panel
        progressPanel.setVisible(true);
        
        // Update the dashboard data
        updateDashboardData();
        
        dashboardVisible = true;
    }
    
    /**
     * Utility method to hide main panels using reflection
     */
    private void hideMainPanels() {
        try {
            // Hide main content panels using reflection
            String[] panelNames = {"jPanel2", "jPanel3", "jPanel5", "jPanel6"};
            for (String panelName : panelNames) {
                java.lang.reflect.Field panelField = MainPage.class.getDeclaredField(panelName);
                panelField.setAccessible(true);
                JPanel panel = (JPanel) panelField.get(mainPage);
                if (panel != null) {
                    panel.setVisible(false);
                }
            }
        } catch (Exception e) {
            // Log the error but continue
            System.err.println("Error hiding main panels: " + e.getMessage());
        }
    }
    
    /**
     * Hides the progress dashboard and shows the main content
     */
    public void hideProgressDashboard() {
        // Hide progress panel
        progressPanel.setVisible(false);
        
        // Show main content using reflection
        showMainPanels();
        
        dashboardVisible = false;
    }
    
    /**
     * Utility method to show main panels using reflection
     */
    private void showMainPanels() {
        try {
            // Show main content panels using reflection
            String[] panelNames = {"jPanel2", "jPanel3", "jPanel5", "jPanel6"};
            for (String panelName : panelNames) {
                java.lang.reflect.Field panelField = MainPage.class.getDeclaredField(panelName);
                panelField.setAccessible(true);
                JPanel panel = (JPanel) panelField.get(mainPage);
                if (panel != null) {
                    panel.setVisible(true);
                }
            }
        } catch (Exception e) {
            // Log the error but continue
            System.err.println("Error showing main panels: " + e.getMessage());
        }
    }
    
    /**
     * Updates all the data displayed on the dashboard
     */
    public void updateDashboardData() {
        // Access username through getter method if needed
        String username = getUsername();
        
        // Get data from the SRSProgressTracker
        Map<Integer, Integer> levelCounts = SRSProgressTracker.getSrsLevelCounts(username);
        int dueCards = SRSProgressTracker.getDueCardCount(username);
        int totalCardCount = SRSProgressTracker.getTotalCardCount(username);
        Map<String, Integer> weeklyStats = SRSProgressTracker.getWeeklyStats(username);
        double masteryPercentage = SRSProgressTracker.calculateMasteryPercentage(username);
        
        // Update the level bars
        updateLevelProgressBars(levelCounts, totalCardCount);
        
        // Update statistics panel
        totalCardsValue.setText(String.valueOf(totalCardCount));
        reviewedCardsValue.setText(String.valueOf(weeklyStats.getOrDefault("reviewed", 0)));
        masteredCardsValue.setText(String.valueOf(weeklyStats.getOrDefault("mastered", 0)));
        
        // Update due today panel
        dueCardCount.setText(String.valueOf(dueCards));
        int masteryPercent = (int) Math.round(masteryPercentage);
        masteryProgressBar.setValue(masteryPercent);
        this.masteryPercent.setText(masteryPercent + "%");
        
        // Enable/disable study button based on due cards
        studyDueCardsButton.setEnabled(dueCards > 0);
    }
    
    /**
     * Updates the progress bars for each SRS level
     */
    private void updateLevelProgressBars(Map<Integer, Integer> levelCounts, int totalCardCount) {
        if (totalCardCount > 0) {
            // Calculate percentages for each level
            int level0Count = levelCounts.getOrDefault(0, 0);
            int level1Count = levelCounts.getOrDefault(1, 0);
            int level2Count = levelCounts.getOrDefault(2, 0);
            int level3Count = levelCounts.getOrDefault(3, 0);
            int level4Count = levelCounts.getOrDefault(4, 0);
            int level5Count = levelCounts.getOrDefault(5, 0);
            
            // Update progress bars
            level0Bar.setValue((int) ((level0Count / (double) totalCardCount) * 100));
            level1Bar.setValue((int) ((level1Count / (double) totalCardCount) * 100));
            level2Bar.setValue((int) ((level2Count / (double) totalCardCount) * 100));
            level3Bar.setValue((int) ((level3Count / (double) totalCardCount) * 100));
            level4Bar.setValue((int) ((level4Count / (double) totalCardCount) * 100));
            level5Bar.setValue((int) ((level5Count / (double) totalCardCount) * 100));
            
            // Update tooltips with actual count
            level0Bar.setToolTipText(level0Count + " cards");
            level1Bar.setToolTipText(level1Count + " cards");
            level2Bar.setToolTipText(level2Count + " cards");
            level3Bar.setToolTipText(level3Count + " cards");
            level4Bar.setToolTipText(level4Count + " cards");
            level5Bar.setToolTipText(level5Count + " cards");
        } else {
            // Reset progress bars if there are no cards
            level0Bar.setValue(0);
            level1Bar.setValue(0);
            level2Bar.setValue(0);
            level3Bar.setValue(0);
            level4Bar.setValue(0);
            level5Bar.setValue(0);
        }
    }
    
    /**
     * Starts a study session with cards that are due today
     */
    public void studyDueCards() {
        try {
            String username = getUsername();
            int dueCount = SRSProgressTracker.getDueCardCount(username);
            
            if (dueCount > 0) {
                // Show a confirmation dialog
                int confirm = JOptionPane.showConfirmDialog(
                    this.mainPage,
                    "You have " + dueCount + " cards due for review. Would you like to study them now?",
                    "Study Due Cards",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // TODO: Implement logic to open FlashcardSelector with due cards filter
                    // This would require modifications to the FlashcardSelector to handle filtering
                    // For now, just open the flashcard selector
                    FlashcardSelector fs = new FlashcardSelector(this.mainPage, username, null, null, null, 0);
                    fs.setVisible(true);
                    this.mainPage.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(
                    this.mainPage,
                    "You don't have any cards due for review today.",
                    "No Due Cards",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this.mainPage,
                "Error loading due cards: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Utility method to safely get the username from MainPage
     */
    private String getUsername() {
        try {
            // Try to access the username field via reflection
            java.lang.reflect.Field usernameField = MainPage.class.getDeclaredField("username");
            usernameField.setAccessible(true);
            return (String) usernameField.get(mainPage);
        } catch (Exception e) {
            // If unable to access, return a default value
            return "user";
        }
    }
}