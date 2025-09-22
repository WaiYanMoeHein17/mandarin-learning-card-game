package projects;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * The fileReplacer class handles modifications to the flagged terms text file.
 * This class is responsible for updating specific terms in the flagged terms list,
 * which tracks words or phrases that have been marked for special attention
 * during the learning process.
 * 
 * The class reads the entire flaggedTerms.txt file, modifies the specified entry,
 * and writes back the updated content. Each entry in the file consists of:
 * - A set number identifier
 * - Followed by the flagged terms for that set
 */
public class fileReplacer {
    /** List to store all lines from the flaggedTerms.txt file */
    private ArrayList<String> csvLines = new ArrayList<>();
    
    /** The set number to locate in the file */
    private int setNum;
    
    /** The index offset from the set number line to modify */
    private int index;
    
    /** The new text to replace at the specified location */
    private String replace;

    /**
     * Creates a new fileReplacer instance and immediately performs the file update.
     * 
     * @param sn The set number to locate in the file
     * @param index The index offset from the set number to modify
     * @param txt The new text to insert at the specified location
     */
    public fileReplacer(int sn, int index, String txt) {
        setNum = sn;
        this.index = index;
        replace = txt;
        
        readFlaggedTerms();
        updateFlaggedTerm();
        writeFlaggedTerms();
    }
    
    /**
     * Reads all lines from the flaggedTerms.txt file into memory.
     */
    private void readFlaggedTerms() {
        String fileName = "flaggedTerms.txt";
        try (Scanner s = new Scanner(new FileReader(fileName))) {
            while (s.hasNext()) {
                String line = s.nextLine();
                csvLines.add(line);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                null,
                "Could not find flaggedTerms.txt file: " + e.getMessage(),
                "WARNING",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    /**
     * Updates the specified term in the loaded file contents.
     */
    private void updateFlaggedTerm() {
        for (int i = 0; i < csvLines.size(); i++) {
            if (csvLines.get(i).equals(String.valueOf(setNum))) {
                csvLines.set(index + i, replace);
            }
        }
    }
    
    /**
     * Writes the updated contents back to the flaggedTerms.txt file.
     */
    private void writeFlaggedTerms() {
        try (FileWriter fr = new FileWriter("flaggedTerms.txt", false);
             BufferedWriter br = new BufferedWriter(fr)) {
             
            for (String line : csvLines) {
                br.write(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Error writing to flaggedTerms.txt: " + e.getMessage(),
                "WARNING",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
