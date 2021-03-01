/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Resizing array implementation of a randomized queue.
public class RandomizedQueue<Item> implements Iterable<Item> {

    // initial capacity of underlying resizing array
    private static final int INIT_CAPACITY = 8;

    private Item[] queue;       // queue elements
    private int n;          // number of elements on queue
    private int first;      // index of first element of queue
    private int last;       // index of next available slot

    public RandomizedQueue() {
        queue = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = queue[(first + i) % queue.length];
        }
        queue = copy;
        first = 0;
        last = n;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Can't add null to queue");

        // double size of array if necessary and recopy to front of array
        if (n == queue.length) resize(2 * queue.length);   // double size of array if necessary
        queue[last++] = item;                        // add item

        // if (last == queue.length) last = n;          // wrap-around
        n++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Empty queue");

        int randomIndex = StdRandom.uniform(first, last);
        Item randomValue = queue[randomIndex];
        Item firstValue = queue[first];
        queue[randomIndex] = firstValue;

        queue[first] = null;                            // to avoid loitering
        n--;
        first++;

        if (first == queue.length) first = 0;           // wrap-around
        // shrink size of array if necessary
        if (n > 0 && n == queue.length / 4) resize(queue.length / 2);

        return randomValue;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Empty deque");

        int randomIndex = StdRandom.uniform(first, last);
        Item item = queue[randomIndex];
        return item;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] randomQueue;

        public RandomizedQueueIterator() {
            randomQueue = (Item[]) new Object[n];

            for (int x = 0; x < n; x++) {
                randomQueue[x] = queue[(first + x) % queue.length];
            }
            StdRandom.shuffle(randomQueue);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements remaining");
            Item item = randomQueue[(i + first) % randomQueue.length];
            i++;
            return item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> deque = new RandomizedQueue<String>();
        deque.enqueue("A");
        deque.enqueue("B");
        deque.enqueue("C");

        StdOut.println(deque.size());
        StdOut.println(deque.dequeue());
        StdOut.println(deque.size());
        StdOut.println(deque.sample());
        StdOut.println(deque.sample());
        StdOut.println(deque.sample());

        deque.enqueue("D");
        deque.enqueue("E");
        deque.enqueue("F");
        deque.enqueue("G");
        deque.enqueue("H");
        deque.enqueue("I");
        StdOut.println(deque.size());
        for (String element : deque) {
            StdOut.println(element);
        }

        RandomizedQueue<String> newDeque = new RandomizedQueue<String>();
        newDeque.enqueue("AA");
        newDeque.enqueue("BB");
        newDeque.enqueue("BB");
        newDeque.enqueue("BB");
        newDeque.enqueue("CC");
        newDeque.enqueue("BB");
        StdOut.println(newDeque.size());
        for (String element : newDeque) {
            StdOut.println(element);
        }
    }
}

