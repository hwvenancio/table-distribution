package br.wedding;

public class Table {

    public final int capacity;
    public final Guest[] guests;

    public Table(int capacity) {
        this(capacity, new Guest[0]);
    }

    public Table(int capacity, Guest[] guests) {
        this.capacity = capacity;
        this.guests = guests;
    }

    public int count() {
        return capacity;
    }
}
