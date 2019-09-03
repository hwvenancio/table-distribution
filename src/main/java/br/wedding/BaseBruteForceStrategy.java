package br.wedding;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class BaseBruteForceStrategy implements Distribution.Strategy {

    private final BiFunction<Guest[], Table[], Iterator<Guest[]>> permutationGenerator;

    protected BaseBruteForceStrategy(BiFunction<Guest[], Table[], Iterator<Guest[]>> permutationGenerator) {
        this.permutationGenerator = permutationGenerator;
    }

    @Override
    public Stream<Table> calculate(Guest[] guests, Table[] tables) {
        Iterator<Guest[]> it = allCombinations(guests, tables);
        int maxScore = Integer.MIN_VALUE;
        Guest[] maxCombination = guests;
        while(it.hasNext()) {
            Guest[] combination = it.next();
            int score = calculateScore(combination, tables);
            if(score > maxScore) {
                maxCombination = combination.clone();
                maxScore = score;
            }
        }
        if(maxScore == Integer.MIN_VALUE)
            return Stream.empty();

        return Arrays.stream(outputTables(maxCombination, tables));
    }

    private int calculateScore(Guest[] guests, Table[] tables) {
        int sum = 0;
        int index = 0;
        for (Table table : tables) {
            int score = calculateScore(guests, index, table.capacity);
            if (score == 0)
                return 0;
            sum += score;
            index += table.capacity;
        }
        return sum;
    }

    private int calculateScore(Guest[] guests, int start, int count) {
        int endExclusive = start + count;
        int emptySpaces = (int) IntStream.range(start, endExclusive)
                .filter(i -> guests[i] == null)
                .count();

        if(emptySpaces == count)
            return count;

        Map<Map.Entry<String, String>, Long> matchingTags = Arrays.stream(guests, start, endExclusive)
                .filter(Objects::nonNull)
                .flatMap(g -> g.tags.entries().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return matchingTags.values().stream()
                .filter(v -> v > 1)
                .mapToInt(Long::intValue)
                .sum() - (emptySpaces * emptySpaces);
    }

    private Iterator<Guest[]> allCombinations(Guest[] guests, Table[] tables) {
        int totalSpaces = Arrays.stream(tables)
                .mapToInt(t -> t.capacity)
                .sum();

        int max = Math.max(guests.length, totalSpaces);

        return permutationGenerator.apply(Arrays.copyOf(guests, max), tables);
    }

    private Table[] outputTables(Guest[] guests, Table[] tables) {
        AtomicInteger totalCount = new AtomicInteger();
        return Arrays.stream(tables)
                .map(table -> {
                    int lower = totalCount.getAndAdd(table.capacity);
                    return new Table(table.capacity, copyOfRange(guests, lower, table.capacity));
                })
                .toArray(Table[]::new);
    }

    private static Guest[] copyOfRange(Guest[] elements, int start, int count) {
        return Arrays.stream(elements)
                .skip(start)
                .limit(count)
                .filter(Objects::nonNull)
                .toArray(Guest[]::new);
    }

}
