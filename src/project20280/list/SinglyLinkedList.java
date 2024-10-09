package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {
        int length = 0;
        if (head == null){
            return length;
        }

        Node<E> currentNode = head;

        while (currentNode != null){
            length++;
            currentNode = currentNode.next;
        }
        return length;
    }

    //@Override
    public boolean isEmpty() {
        int size = size();
        return size == 0;
    }

    @Override
    public E get(int position) {
        if (head == null){
            return null;
        }

        Node<E> walk = head;

        for (int i = 0; i < position; i++){
            walk = walk.getNext();
        }

        return walk.getElement();
    }

    @Override
    public void add(int position, E e) {
        if (head == null){
            throw new IllegalArgumentException();
        }

        Node<E> walk = head;

        for (int i = 0; i < position-1; i++){
            walk = walk.getNext();
        }

        Node<E> newest = new Node<E>(e,walk.getNext());
        walk.setNext(newest);


    }


    @Override
    public void addFirst(E e) {

        head = new Node<E>(e, head); // create and link a new node
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> newest = new Node<E>(e, null);
        if (head == null){
            head = newest;
        }
        else {
            Node<E> last = getLast(e);


            last.setNext(newest);
        }

        size++;
    }

    public Node<E> getLast(E e){


        Node<E> current = head;

        while (current.getNext() != null) {
            current = current.getNext();
        }

        return current;


    }

    @Override
    public E remove(int position) {
        if (head == null) {
            return null;
        }

        Node<E> current = head;

        for (int i = 0; i < position; i++){
            current = current.getNext();
        }

        Node<E> previous = head;

        for (int i = 0; i < position-1; i++){
            previous = previous.getNext();
        }

        previous.setNext(current.getNext());





        return current.getElement();
    }

    @Override
    public E removeFirst() {
        if (head == null){
            return null;
        }

        Node<E> first = head;
        head = head.getNext();



        return first.getElement();
    }

    @Override
    public E removeLast() {

        Node<E> current = head;
        Node<E> previous = head;
        int count = 0;

        if (size() == 1){
            head = null;

            return null;
        }

        while (current.getNext() != null) {
            current = current.getNext();
            count++;
        }

        for (int i = 0; i < count - 1 ; i++){
            previous = previous.getNext();
        }

        previous.setNext(null);
        size--;


        return current.getElement();
    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());
        //LinkedList<Integer> ll = new LinkedList<Integer>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);
        //ll.removeLast();
        //ll.removeFirst();
        //System.out.println("I accept your apology");
        //ll.add(3, 2);
        System.out.println(ll);
        ll.remove(5);
        System.out.println(ll);

//        for(Integer i : ll) {
//            System.out.println(i);
//        }
        /*
        ll.addFirst(-100);
        ll.addLast(+100);
        System.out.println(ll);

        ll.removeFirst();
        ll.removeLast();
        System.out.println(ll);

        // Removes the item in the specified index
        ll.remove(2);
        System.out.println(ll);
        
         */
    }
}
