package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private final E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public void setNext(DoublyLinkedList.Node<E> n) {
            next = n;
        }

        public void setPrev(DoublyLinkedList.Node<E> p) {
            prev = p;
        }
    }
    private final Node<E> head;
    private final Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> newest = new Node<>(e, pred, succ);
        pred.setNext(newest);
        succ.setPrev(newest);
        size++;
    }



    @Override
    public int size() {
        int length = 0;

        DoublyLinkedList.Node<E> currentNode = head;

        while (currentNode != null){
            length++;
            currentNode = currentNode.next;
        }
        return length - 2;

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public E get(int i) {
        Node<E> walker = head;

        for (int j = 0; j < i + 1; j++){
            walker = walker.getNext();
        }

        return walker.data;
    }

    @Override
    public void add(int i, E e) {
        Node<E> walker = head;

        for (int j = 0; j < i; j++){
            walker = walker.getNext();
        }

        addBetween(e, walker, walker.getNext());

    }

    @Override
    public E remove(int i) {
        Node<E> walker = head;

        for (int j = 0; j < i + 1; j++){
            walker = walker.getNext();
        }

        Node<E> prewalk = walker.getPrev();
        Node<E> postwalk = walker.getNext();

        prewalk.setNext(postwalk);
        postwalk.setPrev(prewalk);


        size--;
        return walker.data;

    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

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
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {


        Node<E> prewalk = n.getPrev();
        Node<E> postwalk = n.getNext();

        prewalk.setNext(postwalk);
        postwalk.setPrev(prewalk);


        size--;


        return n.data;
    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        Node<E> first= head.getNext();


        return first.data;
    }

    public E last() {
        if (isEmpty()) {
            return null;
        }
        Node<E> last = tail.getPrev();


        return last.data;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) return null; // nothing to remove



        return remove(head.getNext());
    }

    @Override
    public E removeLast() {
        if (isEmpty()) return null; // nothing to remove


        return remove(tail.getPrev());

    }

    @Override
    public void addLast(E e) {
        addBetween(e, tail.getPrev(), tail);

    }

    @Override
    public void addFirst(E e) {
        addBetween(e, head, head.getNext());


    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {

    }
}