package projects;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class fileReplacer {
    
    private ArrayList<String> csvLines = new ArrayList();
    private int setNum;
    private int index;
    private String replace;

    public fileReplacer(int sn, int index, String txt){
        setNum=sn;
        this.index=index;
        replace=txt;
        
        String fileName = "flaggedTerms.txt";
        try{
            Scanner s = new Scanner(new FileReader(fileName));
            while(s.hasNext()){
                String line = s.nextLine();
                csvLines.add(line);
            }

        }catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        for(int i=0; i<csvLines.size();i++){
            //System.out.println(csvLines.get(i));
            if(csvLines.get(i).equals(""+setNum)){
                //System.out.println(csvLines.get(1+index));
                csvLines.set(index+i,replace);
                //System.out.println(csvLines.get(i+index));
            }
        }
        try{
            FileWriter fr = new FileWriter("flaggedTerms.txt",false);
            BufferedWriter br = new BufferedWriter(fr);
            for(int i=0; i<csvLines.size();i++){
                br.write(""+csvLines.get(i)+"\n");
            }
            br.close();
            fr.close();
            System.out.println("sucess");
        }catch(IOException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
    }
    
}
