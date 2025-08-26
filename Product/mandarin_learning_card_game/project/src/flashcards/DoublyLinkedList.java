
package flashcards;

public class DoublyLinkedList {
    //intialise instance variables
    private Node head;
    private Node tail;
    
    //Method: constructor
    public DoublyLinkedList(){
        //Head is null when created
        head = null;
        //When empty head and tail are the same
        tail = head;       
    }
    
    //Method: Output first data
    public Cards displayHead(){
        //if the head is empty, output empty
        if(head==null){
                //System.out.println("Empty");
            return(null);
            //else output the data in node
        }else{
                //System.out.println(head.getData());
            return(head.getData());
        }
    }
    
    //Get the starting node
    public Node getHead(){
        //if not empty
        if(head == null){
            return(null);
        }
        //else return the starting node
        else{
            return(head);
        }
    }
    
    //Method: output last data
    public Cards displayTail(){
        //if the tail is empty output empty
        if(tail==null){
            System.out.println("Empty");
            return(null);
        //else output the data in the node
        }else{
            System.out.println(tail.getData());
            return(tail.getData());
        }
    }
    
    //Method: add data to end
    public void additem(Cards s){
        //if the head is empty
        if(head==null){
            //New item is the head
            Node temp = new Node(s);
            head = temp;
            //As only one node head and tail are the smae
            tail=head;
        }else{
            //Add new node to the end
            Node temp = new Node(s);
            tail.setNext(temp);
            temp.setPrev(tail);
            //tail is the last node (new one)
            tail=temp;
        }                 
    }
    
    //Method: Output all the data (for testing)
    public void printInOrder(){
        System.out.println("Display Head to tail");
        //create temp node
        Node c = head;      
        //while c has a next (or c next is not null)
            while(c.getNext() != null){
                //print data from c
                System.out.println(c.getData());
                c=c.getNext();
        }     
    }  
    
    //Method: output number of terms (for the counter)
    public int count(){
        //temp node starts at begining
        Node c = head;
        int count = 1;
        //interate through until no more nodes
        while(c.getNext() != null){
            //add one to count each time
            count=count+1;
            c=c.getNext();
        }
        //send count back
        return count+1;
    }
    
    //Method: Search for a term (for testing)
    public Boolean search(String word){
        //Temporary node
        Node c = head;
        //Is not found until found so set to false
        boolean found = false;
        //get data from each node till search term = data
        while(found == false){
            if(c != null){
                if(c.getData().equals(word)){
                    found = true;
                }else{
                    c=c.getNext();
                }
            }else{
                //If found finished
                break;
            }
        }
        //Return output
        return found;
    }
    
}
