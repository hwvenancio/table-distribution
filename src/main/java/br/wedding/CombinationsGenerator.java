package br.wedding;

import org.apache.commons.lang3.Validate;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CombinationsGenerator {

    public static List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
    }

    public static <T> void bla(T[] elements, int[] buckets) {
        int sum = Arrays.stream(buckets).sum();
        Validate.isTrue(elements.length == sum, "Elements length must be equal buckets sum");

        Integer[] something = IntStream.range(0, buckets.length)
                .flatMap(i -> IntStream.range(0, buckets[i]).map(x -> i))
                .boxed()
                .toArray(Integer[]::new);

        List<List<Integer>> combos = Generator.permutation(something)
                .simple()
                .stream()
                .collect(Collectors.toList());

        System.out.println(combos);
    }

    public static <T> Iterator<T[]> grouped(T[] elements, int[] buckets) {
        Validate.isTrue(elements.length == Arrays.stream(buckets).sum(), "Elements length must be equal buckets sum");
        return new GroupedCombinationsGenerator<>(elements, buckets);
    }

    private static class GroupedCombinationsGenerator<T> implements Iterator<T[]> {

        private final T[] elements;
        private final int[] buckets;
        private final T[] output;
        private final Iterator<List<Integer>> iterator;
        private final int[] temp;

        public GroupedCombinationsGenerator(T[] elements, int[] buckets) {
            this.elements = elements;
            this.buckets = buckets;
            this.output = elements.clone();
            Integer[] something = IntStream.range(0, buckets.length)
                    .flatMap(i -> IntStream.range(0, buckets[i]).map(x -> i))
                    .boxed()
                    .toArray(Integer[]::new);

            temp = new int[buckets.length];

            iterator = Generator.permutation(something)
                    .simple()
                    .iterator();

        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            resetTemp();

            List<Integer> indices = iterator.next();
            for(int i = 0; i < indices.size(); ++i) {
                int group = indices.get(i);
                int space = temp[group]++;
                output[space] = elements[i];
            }
            return output;
        }

        void resetTemp() {
            int offset = 0;
            for(int i = 0; i < buckets.length; ++i) {
                temp[i] = offset;
                offset += buckets[i];
            }
        }
    }

}
