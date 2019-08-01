package br.wedding;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BruteForceStrategy implements Distribution.Strategy {
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

        PermutationsGenerator<Guest> generator = new PermutationsGenerator<>(Arrays.copyOf(guests, max));

        Iterable<Guest[]> iterable = () -> generator;
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

    private Guest[] copyOfRange(Guest[] guests, int lower, int upper) {
        return Arrays.stream(guests)
                .skip(lower)
                .limit(upper)
                .filter(Objects::nonNull)
                .toArray(Guest[]::new);
    }

    private static class PermutationsGenerator<T> implements Iterator<T[]> {

        private int i = 0;
        private final int n;
        private final T[] elements;
        private final int[] indices;

        public PermutationsGenerator(T[] elements) {
            this.n = elements.length;
            indices = new int[n];
            this.elements = elements;
        }

        @Override
        public boolean hasNext() {
            return i < n;
        }

        @Override
        public T[] next() {
            if(i == -1) {
                ++i;
                return elements;
            } else if (indices[i] < i) {
                swap(elements, i % 2 == 0 ? 0 : indices[i], i);
                ++indices[i];
                i = 0;
            } else {
                indices[i] = 0;
                ++i;
            }
            return elements;
        }

        private void swap(T[] input, int a, int b) {
            T tmp = input[a];
            input[a] = input[b];
            input[b] = tmp;
        }
    }

}
