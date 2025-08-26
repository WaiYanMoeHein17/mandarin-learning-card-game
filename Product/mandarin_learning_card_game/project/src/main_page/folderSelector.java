
package main_page;
 
public class folderSelector {
    
    public String setNumbers;
    public int foldernumber;
    public String folderName;
    
    public folderSelector(String s, int f, String fn){
        setNumbers=s;
        foldernumber=f;
        folderName=fn;
    }
    
    public String getsetNumbers(){
        return(setNumbers);
    }
    
    public int getfoldernumber(){
        return(foldernumber);
    }
    
    public String getfolderName(){
        return(folderName);
    }
    
    public void setsetNumbers(String x){
        setNumbers = x;
    }
}
