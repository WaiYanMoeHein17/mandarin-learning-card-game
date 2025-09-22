
package flashcards;

/**
 * A doubly-linked list implementation for managing flashcards in the Mandarin learning game.
 * This list maintains a sequence of Card objects, where each node is connected to both its
 * previous and next nodes, allowing for bi-directional traversal of the flashcard set.
 * 
 * Key features:
 * - Maintains head and tail pointers for quick access to list ends
 * - Supports adding cards at the end of the list
 * - Provides methods for traversing and displaying cards
 * - Includes search functionality for finding specific cards
 * - Tracks the total number of cards in the list
 * 
 * The list is primarily used to store and manage the user's flashcard collection,
 * enabling features like forward/backward navigation and card counting.
 */
public class DoublyLinkedList {
    /** The first node in the list, or null if the list is empty */
    private Node head;
    
    /** The last node in the list, or null if the list is empty */
    private Node tail;
    
    /**
     * Creates an empty doubly-linked list with null head and tail.
     */
    public DoublyLinkedList() {
        // Initialize an empty list
        head = null;
        // When empty head and tail are the same
        tail = head;       
    }
    
    /**
     * Gets the first card in the list without removing it.
     * 
     * @return The first Card object in the list, or null if the list is empty
     */
    public Cards displayHead() {
        if (head == null) {
            return null;
        } else {
            return head.getData();
        }
    }
    
    /**
     * Gets the first node in the list for traversal purposes.
     * 
     * @return The head Node of the list, or null if the list is empty
     */
    public Node getHead() {
        if (head == null) {
            return null;
        } else {
            return head;
        }
    }
    
    /**
     * Gets the last card in the list without removing it.
     * 
     * @return The last Card object in the list, or null if the list is empty
     */
    public Cards displayTail() {
        if (tail == null) {
            System.out.println("Empty");
            return null;
        } else {
            System.out.println(tail.getData());
            return tail.getData();
        }
    }
    
    /**
     * Adds a new card to the end of the list.
     * If the list is empty, the new card becomes both head and tail.
     * Otherwise, the card is added after the current tail.
     * 
     * @param s The Card object to add to the list
     */
    public void additem(Cards s) {
        if (head == null) {
            // New item becomes both head and tail in empty list
            Node temp = new Node(s);
            head = temp;
            tail = head;
        } else {
            // Add new node after current tail
            Node temp = new Node(s);
            tail.setNext(temp);
            temp.setPrev(tail);
            tail = temp;
        }                 
    }
    
    /**
     * Prints all cards in the list from head to tail.
     * Used for testing and debugging purposes.
     */
    public void printInOrder() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }
        
        System.out.println("Display Head to tail");
        Node current = head;      
        while (current != null) {
            System.out.println(current.getData());
            current = current.getNext();
        }     
    }  
    
    /**
     * Counts the total number of cards in the list.
     * 
     * @return The total number of cards in the list, or 0 if empty
     */
    public int count() {
        if (head == null) {
            return 0;
        }
        
        Node current = head;
        int count = 1;
        while (current.getNext() != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }
    
    /**
     * Searches for a card in the list that matches the given term.
     * 
     * @param word The term to search for
     * @return true if a card containing the term is found, false otherwise
     */
    public boolean search(String word) {
        if (head == null || word == null) {
            return false;
        }
        
        Node current = head;
        while (current != null) {
            Cards card = current.getData();
            if (card != null && card.getTerms() != null && card.getTerms().contains(word)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
    
}
