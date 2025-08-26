
package main_page;

    public class SetSelector{
            
            //stored variables
            private int setNumber;
            private String setCreator;
            private boolean validity;
            private String access;
            private String password;
            private String[] tableTitles;
            private String inputTerms;
            private String nameOfSet;
            private String notesOfSet;
            private String topicOfSet;
            private String dateC;
            private String dateU;

            //store the values that each set has
            
            public SetSelector(int setNumber,String setCreator,boolean validity,String access,String password, String[] tableTitles, String inputTerms, String nameOfSet, String notesOfSet, String topicOfSet,String dateC, String dateU){
                this.setNumber=setNumber;
                this.setCreator=setCreator;
                this.validity = validity;
                this.access=access;
                this.password = password;
                this.tableTitles = tableTitles;
                this.inputTerms = inputTerms;
                this.nameOfSet = nameOfSet;
                this.notesOfSet=notesOfSet;
                this.topicOfSet=topicOfSet;
                this.dateC = dateC;
                this.dateU = dateU;
            }
            
            //get methods for each set
            
            public int getSetNumber(){
                return(setNumber);
            }
            
            public boolean getValidity(){
                return(validity);
            }
            public String[] getTableTitles(){
                return(tableTitles);
            }
            
            public String getSetCreator(){
                return(setCreator);
            }
            
            public String getAccess(){
                return(access);
            }
            
            public String getPassword(){
                return(password);
            }
            
            public String getInputTerms(){
                return(inputTerms);
            }
            
            public String getNameOfSet(){
                return(nameOfSet);
            }
            
            public String getNotesOfSet(){
                return(notesOfSet);
            }
            
            public String getTopicOfSet(){
                return(topicOfSet);
            }
            
            public void setDateC(String date){
                dateC=date;
            }
            
            public String getDateC(){
                return(dateC);
            }
            
            public void setDateU(String date){
                dateU=date;
            }
            
            public String getDateU(){
                return(dateU);
            }
            
            
    }
