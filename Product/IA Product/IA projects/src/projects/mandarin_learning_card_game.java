
package projects;

import login.LoginScreen;
import main_page.MainPage;
import flashcards.Cards;
import creator.createSet;
import flashcards.FlashCards;
import flashcards.FlashcardSelector;
import javax.swing.JOptionPane;


public class mandarin_learning_card_game {


    public static void main(String[] args) {
                
        LoginScreen ls = new LoginScreen();
        ls.setVisible(true);
        
        //String access="public";
        //String setCreator = "Jimmy";
        //boolean validated = true;
        /*
        
                
        //createSet cs = new createSet(currentUser); 
        //cs.setVisible(true);
        
        
        FlashcardSelector tester = new FlashcardSelector();
        tester.setsetCreator("Jimmy");
        tester.setValidated(true);
        tester.setAccess("Protected");
        tester.setCurrentUser(currentUser);
        tester.setPassword("hi");
        
        if(tester.check()==true){
                
        String data = "2taran,lt,/president,student,/";
        String data3 = "2taran,lt,mc,/president,student,teacher,/";
        String[] array ={"great","good","meh","bad","horrid"};
        String data4 = "3taran,lt,mc,1,/president,student,teacher,1,/1,2,3,2,/";
        String data2 = "5apple,banana,carrot,pineapple,potato,/f,f,v,f,v,/2,3,4,5,6,/nah,yum,alirght,nah,yum,/taran,lt,colin,flembo,me,/";
        String[] titles = {"name","f or v","cost","rating","rater"};
        String[] titles2 = {"name","role"};
        
        
        //openFlashcardSelector(data2,"food n stuff", "Jimmy","This is a small set explaining what food the CS likes","Food n nutr","12/12/2004","14/23/2012");
        
        
        //tester.settableTitles(titles);
        tester.setVisible(true);
        tester.EnterData(data2,titles);
        tester.resetdisplayStarred();
        //tester.printTerms();
        tester.updateTable();
       
        

        
        tester.setsetName("food and stuff");
        tester.setsetNotes("This is a small set explaining what food the CS likes");
        tester.setTopic("Food and Nutrient");
        tester.setDateCreated("12-12-005");
        tester.setLastUpdated("12-14-2012");
        tester.setStarredName(array);
        tester.decodeStarred("5ffffftttttftftfffttfttfft");
        //tester.decodeStarred("5fffftfffftfffftfffftfffft");
        //tester.decodeStarred("5ttttttttttttttttttttttttt");
        //tester.decodeStarred("4ttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttffttff");
        //tester.decodeStarred("2a1b2c3d4e5f6g7h8j9k0");
        //tester.decodeStarred("1tttttttttttttttt");
        //tester.decodeStarred("2ababab");
        //tester.updateTable();
        }else{
            JOptionPane.showMessageDialog(null,"Sorry you cannot access this set right now", "Access Denied", JOptionPane.ERROR_MESSAGE);
            tester.dispose();
        }
        
        //tester.updateTable();
        
        //openFlashcards mw = new openFlashcards(data);
        //FlashCards fc1 = new FlashCards(data);
        //fc1.setVisible(true);
        //System.out.println(data.length());
        //FlashcardSelector test = new FlashcardSelector(data);
        //test.printTerms();
        //FlashcardSelector test2 = new FlashcardSelector(data2);
        //test2.printTerms();
        
        //boolean[] t = {true,true,false,false,false};
        //boolean[] d = {false,false,true,true,false};
        //boolean[] n = {false,false,false,false,true};
        //char[] s = {'t','f','t','t','f'};
        //Cards tester = new Cards("What is the gradient of a line","give it in the standard from","y=mx+c","where m is the gradient","the derivitive is a constant",s,d,t,n);
        //tester.printTerms();
        //tester.printDefintions();        
        //tester.printNotes();

        */
        //String[] titleNames = {"w","e"};
        //openFlashcardSelector("/","s","s","s","s","2","d",titleNames);
        
    }
    /*
    public static void openFlashcardSelector(String input,String name, String creator, String description, String topic, String dateC, String dateU, String[] titleNames){
        FlashcardSelector fcs = new FlashcardSelector();
        
        fcs.EnterData(input,titleNames);
        fcs.setsetName(name);
        fcs.setsetCreator(creator);
        fcs.setsetNotes(description);
        fcs.setTopic(topic);
        fcs.setDateCreated(dateC);
        fcs.setLastUpdated(dateU);
        
        fcs.updateTable();
        fcs.setVisible(true);
    }
*/
}
 