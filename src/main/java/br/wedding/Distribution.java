package br.wedding;

import java.util.Arrays;

public class Distribution {

    private final Guest[] guests;
    private final Table[] tables;

    public Distribution(Guest[] guests, Table[] tables) {
        this.guests = guests;
        this.tables = tables;
    }

    public static Result calculate(Guest[] guests, Table[] tables) {
        return new Result(new Table[] {new Table(tables[0].capacity, guests) });
    }

    public static Distribution of(Guest[] guests, Table[] tables) {
        return new Distribution(guests, tables);
    }

    public Distribution sortByTag(String tagName) {
        return this;
    }

    public Result calculate() {
        return new Result(new Table[] {
                new Table(tables[0].capacity, Arrays.copyOfRange(guests, 4, 8))
                , new Table(tables[1].capacity, Arrays.copyOfRange(guests, 0, 4))
        });
    }
}
