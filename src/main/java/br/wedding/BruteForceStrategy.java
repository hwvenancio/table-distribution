package br.wedding;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class BruteForceStrategy implements Distribution.Strategy {
    @Override
    public Stream<Table> calculate(Guest[] guests, Table[] tables) {
        return allCombinations(guests, tables)
                .max(Comparator.comparing(combo -> Arrays.stream(combo)
                        .mapToInt(this::calculateScore)
                        .sum())
                )
                .map(Arrays::stream)
                .orElseGet(Stream::empty);
    }

    private int calculateScore(Table table) {
        return 1;
    }

    private Stream<Table[]> allCombinations(Guest[] guests, Table[] tables) {
        AtomicInteger totalCount = new AtomicInteger();
        Table[] newTables = Arrays.stream(tables).map(table -> {
            int lower = totalCount.getAndAdd(table.capacity);
            int upper = Math.min(guests.length, totalCount.get());
            return new Table(table.capacity, Arrays.copyOfRange(guests, lower, upper));
        }).toArray(Table[]::new);
        return Stream.<Table[]>builder()
                .add(newTables)
                .build();
    }
}
