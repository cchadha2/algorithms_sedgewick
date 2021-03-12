/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Linked List implementation of a deque.
public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int n;

    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Can't add null to deque");
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) last = first;
        else {
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        n++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Can't add null to deque");

        Node oldLast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) first = last;
        else {
            last.prev = oldLast;
            oldLast.next = last;
        }
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Empty deque");

        Item item = first.item;
        if (n == 1) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.prev = null;
        }
        n--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Empty deque");

        Item item = last.item;
        if (n == 1) {
            first = null;
            last = null;
        }
        else {
            last.prev.next = null;
            last.prev = null;
        }
        n--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public void remove() {
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements remaining");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("A");
        StdOut.println(deque.first.item);
        StdOut.println(deque.first.next);
        StdOut.println(deque.first.prev);
        StdOut.println(deque.size());

        deque.addFirst("X");
        StdOut.println(deque.first.item);
        StdOut.println(deque.last.item);
        StdOut.println(deque.first.next);
        StdOut.println(deque.last.prev);
        StdOut.println(deque.size());

        deque.addLast("C");
        StdOut.println(deque.last.item);
        StdOut.println(deque.last.prev);
        StdOut.println(deque.size());

        StdOut.println(deque.removeFirst());
        StdOut.println(deque.size());

        StdOut.println(deque.removeFirst());
        StdOut.println(deque.size());

        StdOut.println(deque.last.item);
        StdOut.println(deque.first.item);
        StdOut.println(deque.first.prev);

        StdOut.println(deque.removeFirst());
        StdOut.println(deque.size());
        StdOut.println(deque.first);
        StdOut.println(deque.last);

        deque.addFirst("A");
        StdOut.println(deque.size());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.size());

        deque.addLast("A");
        StdOut.println(deque.size());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.size());

        try {
            deque.addFirst(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Caught exception");
        }
        try {
            deque.addLast(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Caught exception");
        }

        try {
            deque.removeFirst();
        }
        catch (NoSuchElementException e) {
            StdOut.println("Caught exception");
        }
        try {
            deque.removeLast();
        }
        catch (NoSuchElementException e) {
            StdOut.println("Caught exception");
        }

        deque.addLast("A");
        deque.addLast("B");
        deque.addLast("C");
        for (String element : deque) {
            StdOut.println(element);
        }

    }
}
