
package auxillary_functions;

/**
 * MailSelector represents a single mail message in the system.
 * It encapsulates all the properties of a mail message including sender,
 * recipient, content, and metadata like view counts and pinned status.
 * 
 * This class is used by the mail management system to handle message
 * display, storage, and tracking of message states.
 */
public class MailSelector {
    
    /** Unique identifier for the mail message in the database */
    private int mailIndex;
    
    /** Username of the message recipient */
    private String recipient;
    
    /** Username of the message sender */
    private String sender;
    
    /** Subject/topic of the message */
    private String topic;
    
    /** Content of the message */
    private String message;
    
    /** Date when the message was sent (format: dd-MM-yyyy) */
    private String dateSent;
    
    /** Number of times the message has been viewed */
    private int viewTimes;
    
    /** Flag indicating if the message is pinned */
    private boolean pinned;
    
    /**
     * Creates a new MailSelector with the specified properties.
     *
     * @param mi Message index in database
     * @param r Recipient's username
     * @param s Sender's username
     * @param t Message topic/subject
     * @param m Message content
     * @param d Date sent (format: dd-MM-yyyy)
     * @param v Number of times viewed
     * @param p Whether message is pinned
     */
    public MailSelector(int mi, String r, String s, String t, String m, String d, int v, boolean p) {
        this.mailIndex = mi;
        this.recipient = r;
        this.sender = s;
        this.topic = t;
        this.message = m;
        this.dateSent = d;
        this.viewTimes = v;
        this.pinned = p;
    }
    
    /**
     * @return The unique identifier of this message
     */
    public int getMailIndex() {
        return mailIndex;
    }
    
    /**
     * @return The username of the message recipient
     */
    public String getRecipient() {
        return recipient;
    }
    
    /**
     * @return The username of the message sender
     */
    public String getSender() {
        return sender;
    }
    
    /**
     * @return The topic/subject of the message
     */
    public String getTopic() {
        return topic;
    }
    
    /**
     * @return The content of the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @return The date the message was sent (format: dd-MM-yyyy)
     */
    public String getDateSent() {
        return dateSent;
    }
    
    /**
     * @return The number of times this message has been viewed
     */
    public int getViewTimes() {
        return viewTimes;
    }
    
    /**
     * @return true if the message is pinned, false otherwise
     */
    public boolean isPinned() {
        return pinned;
    }
    
    /**
     * Updates the view count for this message.
     *
     * @param count The new view count
     */
    public void setViewTimes(int count) {
        this.viewTimes = count;
    }
}
