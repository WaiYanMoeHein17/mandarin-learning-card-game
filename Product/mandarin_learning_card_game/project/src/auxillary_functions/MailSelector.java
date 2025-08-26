
package auxillary_functions;
 
public class MailSelector {
    
    public int mailIndex ;
    public String recipient;
    public String sender;
    public String topic;
    public String message;
    public String dateSent;
    public int viewTimes;
    public boolean pinned;
    
    public MailSelector(int mi, String r, String s, String t, String m, String d, int v, boolean p){
        mailIndex = mi;
        recipient = r;
        sender = s;
        topic = t;
        message = m;
        dateSent= d;
        viewTimes = v;
        pinned= p;
    }
    
    public int getmailIndex(){
        return(mailIndex);
    }
    
    public String getrecipient(){
        return(recipient);
    }
    
    public String getsender(){
        return(sender);
    }
    
    public String gettopic(){
        return(topic);
    }
    
    public String getmessage(){
        return(message);
    }
    
    public String getdateSent(){
        return(dateSent);
    }
    
    public int getviewTimes(){
        return(viewTimes);
    }
    
    public boolean getpinned(){
        return(pinned);
    }
    
    public void setviewTimes(int x){
        viewTimes = x;
    }
}
