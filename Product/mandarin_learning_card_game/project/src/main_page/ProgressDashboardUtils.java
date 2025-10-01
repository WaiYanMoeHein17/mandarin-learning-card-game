package main_page;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Utility class that adds progress tracking dashboard components to MainPage
 */
public class ProgressDashboardUtils {
    
    /**
     * A modern looking panel with gradient background
     */
    public static class GradientPanel extends JPanel {
        private Color color1 = new Color(41, 128, 185);
        private Color color2 = new Color(52, 152, 219);
        
        public GradientPanel() {
            setOpaque(false);
        }
        
        public void setGradientColors(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, w, h, 15, 15);
            g2d.dispose();
        }
    }
    
    /**
     * A modern progress bar with custom colors
     */
    public static class ModernProgressBar extends JProgressBar {
        private Color progressColor;
        
        public ModernProgressBar(Color progressColor) {
            this.progressColor = progressColor;
            setStringPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setPreferredSize(new Dimension(200, 8));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            
            // Draw progress
            int width = (int) (getWidth() * (getValue() / 100.0));
            g2d.setColor(progressColor);
            g2d.fillRoundRect(0, 0, width, getHeight(), getHeight(), getHeight());
            
            g2d.dispose();
        }
    }
    
    /**
     * A container class to hold all dashboard components
     */
    public static class DashboardComponents {
        public JPanel dashboardPanel;
        public JProgressBar masteryProgressBar;
        public JLabel masteryPercent;
        public JLabel dueCardCount;
        public JButton studyDueCardsButton;
        public JLabel totalCardsValue;
        public JLabel reviewedCardsValue;
        public JLabel masteredCardsValue;
        public JProgressBar[] levelBars;
    }
    
    /**
     * Creates and returns a complete progress dashboard panel with all components
     */
    public static DashboardComponents createDashboardPanel(javax.swing.JFrame parentFrame) {
        // Main panel that will contain the dashboard
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(new Color(250, 250, 250));
        
        // Header with title and back button
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(new Color(250, 250, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Learning Progress Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        
        JButton backButton = new JButton("← Back to Main");
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            // Hide dashboard and show main page content
            dashboardPanel.setVisible(false);
            // Back to main content
            parentFrame.setVisible(true);
        });
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(backButton);
        
        // Mastery overview panel
        GradientPanel masteryPanel = new GradientPanel();
        masteryPanel.setLayout(new BoxLayout(masteryPanel, BoxLayout.Y_AXIS));
        masteryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        masteryPanel.setGradientColors(new Color(41, 128, 185), new Color(52, 152, 219));
        masteryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        masteryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JLabel masteryLabel = new JLabel("Current Mastery");
        masteryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        masteryLabel.setForeground(Color.WHITE);
        masteryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel masteryProgressPanel = new JPanel();
        masteryProgressPanel.setOpaque(false);
        masteryProgressPanel.setLayout(new BoxLayout(masteryProgressPanel, BoxLayout.X_AXIS));
        masteryProgressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JProgressBar masteryProgressBar = new ModernProgressBar(Color.WHITE);
        masteryProgressBar.setValue(0);
        masteryProgressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        masteryProgressBar.setPreferredSize(new Dimension(300, 15));
        
        JLabel masteryPercent = new JLabel("0%");
        masteryPercent.setFont(new Font("Arial", Font.BOLD, 24));
        masteryPercent.setForeground(Color.WHITE);
        
        masteryProgressPanel.add(masteryProgressBar);
        masteryProgressPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        masteryProgressPanel.add(masteryPercent);
        
        masteryPanel.add(masteryLabel);
        masteryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        masteryPanel.add(masteryProgressPanel);
        
        // Due cards panel
        JPanel dueCardsPanel = new JPanel();
        dueCardsPanel.setLayout(new BoxLayout(dueCardsPanel, BoxLayout.X_AXIS));
        dueCardsPanel.setBackground(new Color(255, 255, 255));
        dueCardsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        dueCardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dueCardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JPanel dueCardsInfoPanel = new JPanel();
        dueCardsInfoPanel.setLayout(new BoxLayout(dueCardsInfoPanel, BoxLayout.Y_AXIS));
        dueCardsInfoPanel.setOpaque(false);
        
        JLabel dueCardsLabel = new JLabel("Cards Due Today");
        dueCardsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dueCardsLabel.setForeground(new Color(44, 62, 80));
        dueCardsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel dueCardCount = new JLabel("0");
        dueCardCount.setFont(new Font("Arial", Font.BOLD, 24));
        dueCardCount.setForeground(new Color(231, 76, 60));
        dueCardCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        dueCardsInfoPanel.add(dueCardsLabel);
        dueCardsInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        dueCardsInfoPanel.add(dueCardCount);
        
        JButton studyDueCardsButton = new JButton("Study Now");
        studyDueCardsButton.setFocusPainted(false);
        studyDueCardsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studyDueCardsButton.setEnabled(false);
        studyDueCardsButton.addActionListener(e -> {
            // Study due cards logic - implement this by calling mainPage methods
            // Open flashcards
            JOptionPane.showMessageDialog(parentFrame, "Open flashcards feature will be implemented by MainPage.");
        });
        
        dueCardsPanel.add(dueCardsInfoPanel);
        dueCardsPanel.add(Box.createHorizontalGlue());
        dueCardsPanel.add(studyDueCardsButton);
        
        // SRS Level distribution panel
        JPanel levelDistributionPanel = new JPanel();
        levelDistributionPanel.setLayout(new BoxLayout(levelDistributionPanel, BoxLayout.Y_AXIS));
        levelDistributionPanel.setBackground(Color.WHITE);
        levelDistributionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        levelDistributionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        levelDistributionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        
        JLabel levelDistLabel = new JLabel("SRS Level Distribution");
        levelDistLabel.setFont(new Font("Arial", Font.BOLD, 16));
        levelDistLabel.setForeground(new Color(44, 62, 80));
        levelDistLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel levelsPanel = new JPanel();
        levelsPanel.setLayout(new BoxLayout(levelsPanel, BoxLayout.Y_AXIS));
        levelsPanel.setOpaque(false);
        levelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Create level bars
        JProgressBar[] levelBars = new JProgressBar[6];
        Color[] levelColors = {
            new Color(231, 76, 60),   // Level 0 - Red
            new Color(230, 126, 34),  // Level 1 - Orange
            new Color(241, 196, 15),  // Level 2 - Yellow
            new Color(46, 204, 113),  // Level 3 - Green
            new Color(52, 152, 219),  // Level 4 - Blue
            new Color(142, 68, 173)   // Level 5 - Purple
        };
        
        String[] levelNames = {
            "New Cards",
            "Level 1 - Learning",
            "Level 2 - Review",
            "Level 3 - Review",
            "Level 4 - Mastering",
            "Level 5 - Mastered"
        };
        
        for (int i = 0; i < 6; i++) {
            JPanel levelBarPanel = new JPanel();
            levelBarPanel.setLayout(new BoxLayout(levelBarPanel, BoxLayout.Y_AXIS));
            levelBarPanel.setOpaque(false);
            levelBarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            levelBarPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
            labelPanel.setOpaque(false);
            
            JLabel levelNameLabel = new JLabel(levelNames[i]);
            levelNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            levelNameLabel.setForeground(new Color(44, 62, 80));
            
            labelPanel.add(levelNameLabel);
            labelPanel.add(Box.createHorizontalGlue());
            
            JProgressBar levelBar = new ModernProgressBar(levelColors[i]);
            levelBar.setValue(0);
            levelBar.setAlignmentX(Component.LEFT_ALIGNMENT);
            levelBars[i] = levelBar;
            
            levelBarPanel.add(labelPanel);
            levelBarPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            levelBarPanel.add(levelBar);
            
            levelsPanel.add(levelBarPanel);
        }
        
        levelDistributionPanel.add(levelDistLabel);
        levelDistributionPanel.add(levelsPanel);
        
        // Statistics panel
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new BoxLayout(statisticsPanel, BoxLayout.Y_AXIS));
        statisticsPanel.setBackground(Color.WHITE);
        statisticsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        statisticsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel statsLabel = new JLabel("Weekly Statistics");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statsLabel.setForeground(new Color(44, 62, 80));
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel statsGridPanel = new JPanel();
        statsGridPanel.setLayout(new BoxLayout(statsGridPanel, BoxLayout.X_AXIS));
        statsGridPanel.setOpaque(false);
        statsGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Total cards statistic
        JPanel totalCardsPanel = createStatPanel("Total Cards", "0", new Color(52, 152, 219));
        
        // Cards reviewed this week
        JPanel reviewedCardsPanel = createStatPanel("Cards Reviewed", "0", new Color(46, 204, 113));
        
        // Cards mastered this week
        JPanel masteredCardsPanel = createStatPanel("Cards Mastered", "0", new Color(142, 68, 173));
        
        statsGridPanel.add(totalCardsPanel);
        statsGridPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        statsGridPanel.add(reviewedCardsPanel);
        statsGridPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        statsGridPanel.add(masteredCardsPanel);
        
        statisticsPanel.add(statsLabel);
        statisticsPanel.add(statsGridPanel);
        
        // Add all panels to the main dashboard panel
        dashboardPanel.add(headerPanel);
        dashboardPanel.add(masteryPanel);
        dashboardPanel.add(dueCardsPanel);
        dashboardPanel.add(levelDistributionPanel);
        dashboardPanel.add(statisticsPanel);
        
        // Create component container
        DashboardComponents components = new DashboardComponents();
        components.dashboardPanel = dashboardPanel;
        components.masteryProgressBar = masteryProgressBar;
        components.masteryPercent = masteryPercent;
        components.dueCardCount = dueCardCount;
        components.studyDueCardsButton = studyDueCardsButton;
        components.totalCardsValue = findLabelInPanel(totalCardsPanel, "value");
        components.reviewedCardsValue = findLabelInPanel(reviewedCardsPanel, "value");
        components.masteredCardsValue = findLabelInPanel(masteredCardsPanel, "value");
        
        // Store level bars
        components.levelBars = new JProgressBar[6];
        for (int i = 0; i < 6; i++) {
            components.levelBars[i] = levelBars[i];
        }
        
        // Hide initially
        dashboardPanel.setVisible(false);
        
        return components;
    }
    
    /**
     * Creates a statistics panel with a colored circle, title, and value
     */
    private static JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(102, 102, 102));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setName("value");  // Set name for easy finding
        
        JPanel circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillOval(0, 0, 20, 20);
                g2d.dispose();
            }
        };
        circlePanel.setOpaque(false);
        circlePanel.setMaximumSize(new Dimension(20, 20));
        circlePanel.setPreferredSize(new Dimension(20, 20));
        circlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(circlePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(valueLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(titleLabel);
        
        return panel;
    }
    
    /**
     * Helper method to find a label by name in a panel
     */
    private static JLabel findLabelInPanel(JPanel panel, String name) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel && name.equals(comp.getName())) {
                return (JLabel) comp;
            }
        }
        return null;
    }
}