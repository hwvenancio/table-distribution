package br.wedding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class Distribution {

    private final Guest[] guests;
    private final Table[] tables;
    private final Strategy strategy;
    private Comparator<Table> sorter;

    public Distribution(Guest[] guests, Table[] tables) {
        this.guests = guests;
        this.tables = tables;
        strategy = new BruteForceStrategy();
        sorter = Comparator.comparing(Table::count);
    }

    public static Result calculate(Guest[] guests, Table[] tables) {
        return of(guests, tables).calculate();
    }

    public static Distribution of(Guest[] guests, Table[] tables) {
        return new Distribution(guests, tables);
    }

    public Distribution sortByTag(String tagName) {
        this.sorter = Comparator.comparing((Table t) -> Arrays.stream(t.guests)
                .flatMap(g -> Optional.ofNullable(g.tags.get(tagName))
                        .map(Collection::stream)
                        .orElseGet(Stream::empty)
                )
                .sorted()
                .findFirst()
                .orElse("")
        );
        return this;
    }

    public Result calculate() {
        return new Result(
                strategy.calculate(this.guests, this.tables)
                        .sorted(sorter)
                        .toArray(Table[]::new)
        );
    }

    public interface Strategy {
        Stream<Table> calculate(Guest[] guests, Table[] tables);
    }
}
