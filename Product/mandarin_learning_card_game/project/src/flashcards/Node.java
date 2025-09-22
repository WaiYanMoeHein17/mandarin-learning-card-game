package flashcards;

/**
 * A Node class representing a single element in a doubly-linked list of flashcards.
 * Each node contains a reference to a Card object and maintains links to both the
 * previous and next nodes in the sequence, enabling bi-directional traversal.
 * 
 * This class is used as the building block for the DoublyLinkedList implementation
 * that manages flashcard sequences in the Mandarin learning game. The bi-directional
 * links allow users to move both forward and backward through their flashcard sets.
 */
public class Node {
    
    /** Reference to the previous node in the doubly-linked list */
    private Node prev;
    
    /** Reference to the next node in the doubly-linked list */
    private Node next;
    
    /** The flashcard data stored in this node */
    private Cards data;
    
    
    /**
     * Creates a new Node with the given flashcard data.
     * Initializes the node as a standalone element with no links
     * (both prev and next are set to null).
     * 
     * @param s The Cards object to store in this node
     */
    public Node(Cards s) {
        data = s;
        prev = null;
        next = null;
    }
    
    /**
     * Sets the reference to the next node in the sequence.
     * 
     * @param n The node that should follow this one
     */
    public void setNext(Node n) {
        next = n;
    }
    
    /**
     * Sets the reference to the previous node in the sequence.
     * 
     * @param n The node that should precede this one
     */
    public void setPrev(Node n) {
        prev = n;
    }
    
    /**
     * Retrieves the flashcard data stored in this node.
     * 
     * @return The Cards object contained in this node
     */
    public Cards getData() {
        return data;
    }
    
    /**
     * Gets the reference to the next node in the sequence.
     * 
     * @return The next Node, or null if this is the last node
     */
    public Node getNext() {
        return next;
    }
    
    /**
     * Gets the reference to the previous node in the sequence.
     * 
     * @return The previous Node, or null if this is the first node
     */
    public Node getPrev() {
        return prev;
    }
}