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

    public static TableBuilder builder() {
        return new TableBuilder();
    }

    public long count() {
        return guests.length;
    }

    public static class TableBuilder {

        private int capacity;

        private TableBuilder() { }

        public TableBuilder withCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Table build() {
            return new Table(capacity);
        }
    }

}
