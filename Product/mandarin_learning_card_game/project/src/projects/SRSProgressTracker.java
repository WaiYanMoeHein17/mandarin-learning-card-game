package projects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import projects.DBConnection;

/**
 * The SRSProgressTracker class provides methods to retrieve and analyze SRS (Spaced Repetition System)
 * data from the database. It handles queries for retrieving progress statistics, due cards,
 * and mastery levels for the progress dashboard.
 * 
 * Note: This is not a standalone class but a utility class with static methods to keep
 * the codebase modular while avoiding additional class dependencies.
 */
public class SRSProgressTracker {
    
    /**
     * Retrieves the count of cards at each SRS level for a specific user.
     * 
     * @param username The username to get SRS data for
     * @return Map containing counts for each SRS level (key: level, value: count)
     */
    public static Map<Integer, Integer> getSrsLevelCounts(String username) {
        Map<Integer, Integer> levelCounts = new HashMap<>();
        
        // Initialize counts for all levels (0-5)
        for (int i = 0; i <= 5; i++) {
            levelCounts.put(i, 0);
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT srs_level, COUNT(*) as count " +
                        "FROM srs_data " +
                        "WHERE username = ? " +
                        "GROUP BY srs_level";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int level = rs.getInt("srs_level");
                int count = rs.getInt("count");
                levelCounts.put(level, count);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error retrieving SRS level counts: " + e.getMessage());
        }
        
        return levelCounts;
    }
    
    /**
     * Gets the count of cards due for review today.
     * 
     * @param username The username to get due cards for
     * @return The number of cards due for review
     */
    public static int getDueCardCount(String username) {
        int dueCount = 0;
        
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) as count " +
                        "FROM srs_data " +
                        "WHERE username = ? AND next_review_date <= CURRENT_DATE";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                dueCount = rs.getInt("count");
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error retrieving due card count: " + e.getMessage());
        }
        
        return dueCount;
    }
    
    /**
     * Gets the total number of cards in the SRS system for a user.
     * 
     * @param username The username to get card count for
     * @return The total number of cards
     */
    public static int getTotalCardCount(String username) {
        int totalCount = 0;
        
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) as count FROM srs_data WHERE username = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                totalCount = rs.getInt("count");
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error retrieving total card count: " + e.getMessage());
        }
        
        return totalCount;
    }
    
    /**
     * Gets statistics about cards learned in the past week.
     * 
     * @param username The username to get statistics for
     * @return Map with learning statistics (key: stat name, value: count)
     */
    public static Map<String, Integer> getWeeklyStats(String username) {
        Map<String, Integer> stats = new HashMap<>();
        
        try {
            Connection conn = DBConnection.getConnection();
            
            // Cards reviewed in the past week
            String sqlReviewed = "SELECT COUNT(*) as count " +
                               "FROM srs_data " +
                               "WHERE username = ? AND last_reviewed_date >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)";
            
            // Cards mastered (reached level 5) in the past week
            String sqlMastered = "SELECT COUNT(*) as count " +
                               "FROM srs_data " +
                               "WHERE username = ? AND srs_level = 5 " + 
                               "AND last_reviewed_date >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)";
            
            // Cards learned (reached at least level 2) in the past week
            String sqlLearned = "SELECT COUNT(*) as count " +
                              "FROM srs_data " +
                              "WHERE username = ? AND srs_level >= 2 " +
                              "AND last_reviewed_date >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)";
            
            // Execute each query and store results
            PreparedStatement pstmt = conn.prepareStatement(sqlReviewed);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.put("reviewed", rs.getInt("count"));
            }
            rs.close();
            
            pstmt = conn.prepareStatement(sqlMastered);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.put("mastered", rs.getInt("count"));
            }
            rs.close();
            
            pstmt = conn.prepareStatement(sqlLearned);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.put("learned", rs.getInt("count"));
            }
            rs.close();
            
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error retrieving weekly stats: " + e.getMessage());
        }
        
        return stats;
    }
    
    /**
     * Gets upcoming review schedule for the next week.
     * 
     * @param username The username to get schedule for
     * @return Map with date and card count (key: date string, value: count)
     */
    public static Map<String, Integer> getUpcomingReviews(String username) {
        Map<String, Integer> schedule = new HashMap<>();
        
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT next_review_date, COUNT(*) as count " +
                        "FROM srs_data " +
                        "WHERE username = ? " +
                        "AND next_review_date BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY) " +
                        "GROUP BY next_review_date " +
                        "ORDER BY next_review_date";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String date = rs.getString("next_review_date");
                int count = rs.getInt("count");
                schedule.put(date, count);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Error retrieving upcoming reviews: " + e.getMessage());
        }
        
        return schedule;
    }
    
    /**
     * Calculates overall mastery percentage based on SRS levels.
     * 
     * @param username The username to calculate mastery for
     * @return A percentage value from 0-100 representing overall mastery
     */
    public static double calculateMasteryPercentage(String username) {
        Map<Integer, Integer> levelCounts = getSrsLevelCounts(username);
        int totalCards = 0;
        double weightedSum = 0;
        
        for (int level = 0; level <= 5; level++) {
            int count = levelCounts.getOrDefault(level, 0);
            totalCards += count;
            weightedSum += count * (level / 5.0);
        }
        
        return totalCards > 0 ? (weightedSum / totalCards) * 100 : 0;
    }
}