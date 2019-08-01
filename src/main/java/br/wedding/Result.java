package br.wedding;

import java.util.Arrays;
import java.util.Objects;

public class Result {

    public final Table[] tables;

    public Result(Table[] tables) {
        this.tables = tables;
    }

    public void print() {
        for(int i = 0; i < tables.length; ++i) {
            System.out.println("Table #" + (i+1));
            Table t = tables[i];
            Arrays.stream(t.guests)
                    .filter(Objects::nonNull)
                    .forEach(guest ->
                            System.out.println("- " + guest.name + ": " + guest.tags.toString()));
        }
    }
}
