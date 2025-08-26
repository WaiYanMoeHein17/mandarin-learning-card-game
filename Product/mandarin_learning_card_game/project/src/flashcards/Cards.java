/*
    what this class does:
        every row of data in the set is made into a card from flashcardSelector
        every card is added to a DDL through a node
        According to the current Node selected, the data from here is displayed in the flashcard class which has the UI
*/
package flashcards;

public class Cards {
    
    //initialise instance variables
    private String[] data = new String[5];
    private int numDefinitions;
    private int numTerms;
    private int numNotes;
    private String terms[];
    private String definitions[];
    private String additionalNotes[];
    private char starred[];
    private int cardNumber;
    
    //Constructor (takes in 5 terms, the starred terms, the terms, ann definitions
    public Cards(String a, String b, String c, String d, String e,char s[],char def[], char term[],char notes[],int cardNo){
        
        //add the terms to an array
        data[0]=a;
        data[1]=b;
        data[2]=c;
        data[3]=d;
        data[4]=e;
        
        //get number of of definitions
        numDefinitions = 0;
        for (int i=0;i<5;i++){
            if(def[i]=='t'){
                numDefinitions = numDefinitions +1;
            }
        }
        
        //make aray for defintions
        definitions = new String[numDefinitions];
        
        //add defintions to array
        int tempCount = 0;
            for (int i=0;i<5;i++){
                if(def[i]=='t'){
                    definitions[tempCount]= data[i];
                    tempCount+=1;
                }
            }
                
        //repeat for terms        
        //get number of of terms
        numTerms = 0;
        for (int i=0;i<5;i++){
            if(term[i]=='t'){
                numTerms = numTerms +1;
            }
        }
        
        //make aray for terms
        terms = new String[numTerms];
        //add terms to array
        tempCount = 0;
            for (int i=0;i<5;i++){
                if(term[i]=='t'){
                    terms[tempCount]=data[i];
                    tempCount+=1;
                }
            }
        
        //repeat for notes        
        //get number of of notes
        tempCount = 0;
        for (int i=0;i<5;i++){
            if(notes[i]=='t'){
                numNotes = numNotes +1;
            }
        }
        
        //make aray for notes
        additionalNotes = new String[numNotes];
        //add notes to array
        tempCount = 0;
            for (int i=0;i<5;i++){
                if(notes[i]=='t'){
                    additionalNotes[tempCount]=data[i];
                    tempCount+=1;
                }
            }    
        // the starred array is copied
        starred = s;
        cardNumber=cardNo;
        //System.out.println("DONE");
        
  
    }
    
    //print the terms
    public void printTerms(){
        //System.out.println("Printing Terms");
        for(int i=0; i<terms.length; i++){
            System.out.println(terms[i]);
        }
    }
    
    //print the definitions
    public void printDefintions(){
        //System.out.println("Printing defs");
        for(int i=0; i<definitions.length; i++){
            System.out.println(definitions[i]);
        }
    }
    
    //print the notes
    public void printNotes(){
        //System.out.println("Printing defs");
        for(int i=0; i<additionalNotes.length; i++){
            //System.out.println(additionalNotes[i]);
        }
    }
    
    public String getTerms(){
        String termStr = "";
        for(int i=0; i<terms.length; i++){
            termStr = termStr + terms[i]+ "\n";
        }
        //System.out.println(termStr);
        return(termStr);
    }
    
    public String getDefinitions(){
        String defStr = "";
        for(int i=0; i<definitions.length; i++){
            defStr = defStr + definitions[i]+ "\n";
        }
        return(defStr);
    }
    
    public String getNotes(){
        String defNotes = "";
        for(int i=0; i<additionalNotes.length; i++){
            defNotes = defNotes + additionalNotes[i]+ "\n";
        }
        return(defNotes);
    }
    
    public char[] getStarred(){
        return(starred);
    }

    public int getCardNumber(){
        return cardNumber;
    }

    /*
    public int getNumberOfRows(){
        int x = terms.length;
        System.out.println(x);
        return(x);
    }
    */
}
  