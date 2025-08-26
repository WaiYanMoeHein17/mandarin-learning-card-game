package flashcards;

public class Node{
    
    //initialise instance variable
    
    private Node prev;
    private Node next;
    private Cards data;
    
    
    // initialise node
    public Node(Cards s){
        data = s;
        // set previous and next to null
        prev = null;
        next = null;
    }
    
    // sets the location of next node
    public void setNext(Node n){
        next = n;
    }
    
    // sets the location of previous node
    public void setPrev(Node n){
        prev = n;
    }
    
    // gets the value of the current node
    public Cards getData(){
        return data;
    }
    
    // gets the location of next node
    public Node getNext(){
        return next;
    }
    
    // gets the location of previous node
    public Node getPrev(){
        return prev;
    }
    
    
    
    
    
}