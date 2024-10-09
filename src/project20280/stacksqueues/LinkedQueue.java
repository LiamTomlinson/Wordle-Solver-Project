package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.DoublyLinkedList;
import project20280.list.SinglyLinkedList;

public class LinkedQueue<E> implements Queue<E> {

    private DoublyLinkedList<E> list = new DoublyLinkedList<>( );
    private DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedQueue() {

    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void enqueue(E e) {
       list.addLast(e);
    }

    @Override
    public E first() {
        return list.get(0);
    }

    @Override
    public E dequeue() {

        return list.removeFirst();
    }

    public String toString() {
        return list.toString();
    }
}
