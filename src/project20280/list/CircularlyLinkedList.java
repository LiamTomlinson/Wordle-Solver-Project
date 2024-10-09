package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class CircularlyLinkedList<E> implements List<E> {

    private class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T e, Node<T> n) {
            data = e;
            next = n;
        }

        public T getData() {
            return data;
        }

        public void setNext(Node<T> n) {
            next = n;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<E> tail = null;
    private int size = 0;

    public CircularlyLinkedList() {

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        Node<E> head = tail.getNext();

        for (int j = 0; j < i; j++){
            head = head.getNext();
        }
        return head.data;
    }

    /**
     * Inserts the given element at the specified index of the list, shifting all
     * subsequent elements in the list one position further to make room.
     *
     * @param i the index at which the new element should be stored
     * @param e the new element to be stored
     */
    @Override
    public void add(int i, E e) {
        Node<E> head = tail.getNext();

        for (int j = 0; j < i - 1; j++){
            head = head.getNext();

        }
        Node<E> next = head.getNext();
        Node<E> newest = new Node<E>(e, next);
        head.setNext(newest);
    }

    @Override
    public E remove(int i) {
        Node<E> head = tail.getNext();
        Node<E> prev = tail.getNext();

        for (int j = 0; j < i; j++){
            head = head.getNext();
            if (j > 0){
                prev = prev.getNext();
            }
        }

        Node<E> next = head.getNext();


        prev.setNext(next);

        size--;
        return head.data;
    }

    public void rotate() {
        if (tail != null) // if empty, do nothing
            tail = tail.getNext();

    }

    private class CircularlyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) tail;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }

    }

    @Override
    public Iterator<E> iterator() {
        return new CircularlyLinkedListIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E removeFirst() {
        if (isEmpty( )) return null;
        Node<E> head = tail.getNext( );
        if (head == tail) tail = null;
        else tail.setNext(head.getNext( ));

        size--;
        return head.data;
    }

    @Override
    public E removeLast() {
        Node<E> head = tail.getNext();
        Node<E> prev = tail.getNext();
        int count = 0;


        while (true) {
            head = head.getNext();
            if (head == tail){
                prev.setNext(tail.getNext());
                size--;


                return head.getData();
            }
            prev = prev.getNext();
        }





    }

    @Override
    public void addFirst(E e) {
        if (size == 0) {
            tail = new Node<>(e, null);
            tail.setNext(tail); // link to itself circularly
        } else {
            Node<E> newest = new Node<>(e, tail.getNext( ));
            tail.setNext(newest);
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        addFirst(e);
        tail = tail.getNext( );
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = tail;
        do {
            curr = curr.next;
            sb.append(curr.data);
            if (curr != tail) {
                sb.append(", ");
            }
        } while (curr != tail);
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<Integer>();
        for (int i = 10; i < 20; ++i) {
            ll.addLast(i);
        }

        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        ll.rotate();
        System.out.println(ll);

        ll.removeFirst();
        ll.rotate();
        System.out.println(ll);

        ll.removeLast();
        ll.rotate();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }

    }
}
