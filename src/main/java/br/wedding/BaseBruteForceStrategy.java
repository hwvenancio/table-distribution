package br.wedding;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class BaseBruteForceStrategy implements Distribution.Strategy {

    private final BiFunction<Guest[], Table[], Iterator<Guest[]>> permutationGenerator;

    protected BaseBruteForceStrategy(BiFunction<Guest[], Table[], Iterator<Guest[]>> permutationGenerator) {
        this.permutationGenerator = permutationGenerator;
    }

    @Override
    public Stream<Table> calculate(Guest[] guests, Table[] tables) {
        return allCombinations(guests, tables)
                .max(Comparator.comparing(this::calculateScore))
                .map(Arrays::stream)
                .orElseGet(Stream::empty);
    }

    private int calculateScore(Table[] tables) {
        return Arrays.stream(tables)
                .mapToInt(this::calculateScore)
                .sum();
    }

    private int calculateScore(Table table) {
        if(table.guests.length == 0)
            return table.capacity;

        int emptySpaces = table.capacity - table.guests.length;

        Map<Map.Entry<String, String>, Long> matchingTags = Arrays.stream(table.guests)
                .flatMap(g -> g.tags.entries().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return matchingTags.values().stream()
                .filter(v -> v > 1)
                .mapToInt(Long::intValue)
                .sum() - (emptySpaces * emptySpaces);
    }

    private Stream<Table[]> allCombinations(Guest[] guests, Table[] tables) {
        int totalSpaces = Arrays.stream(tables)
                .mapToInt(t -> t.capacity)
                .sum();

        int max = Math.max(guests.length, totalSpaces);

        Iterable<Guest[]> iterable = () -> permutationGenerator.apply(Arrays.copyOf(guests, max), tables);
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(permuted -> oneCombination(permuted, tables));
    }

    private Table[] oneCombination(Guest[] guests, Table[] tables) {
        AtomicInteger totalCount = new AtomicInteger();
        return Arrays.stream(tables)
                .map(table -> {
                    int lower = totalCount.getAndAdd(table.capacity);
                    int upper = Math.min(guests.length, totalCount.get());
                    return new Table(table.capacity, copyOfRange(guests, lower, upper));
                })
                .toArray(Table[]::new);
    }

    private static Guest[] copyOfRange(Guest[] elements, int lower, int upper) {
        return Arrays.stream(elements)
                .skip(lower)
                .limit(upper)
                .filter(Objects::nonNull)
                .toArray(Guest[]::new);
    }

}
