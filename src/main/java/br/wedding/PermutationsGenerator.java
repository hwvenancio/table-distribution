package br.wedding;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PermutationsGenerator {

    public static <T> Iterator<T[]> full(T[] elements) {
        return new FullPermutationsGenerator<>(elements);
    }

    public static <T> Iterator<T[]> sparse(T[] elements, int[] buckets) {
        Validate.isTrue(elements.length == Arrays.stream(buckets).sum(), "Elements length must be equal buckets sum");
        return new SparsePermutationsGenerator<>(elements, buckets);
    }

    private static class FullPermutationsGenerator<T> implements Iterator<T[]> {

        private final int permutations;
        private int count;
        private int i = -1;
        private final int n;
        private final T[] elements;
        private final int[] indices;

        public FullPermutationsGenerator(T[] elements) {
            this.n = elements.length;
            indices = new int[n];
            this.elements = elements;
            permutations = factorial(n);
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < permutations;
        }

        @Override
        public T[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            while(i < n) {
                if (i == -1) {
                    ++i;
                    break;
                } else if (indices[i] < i) {
                    swap(elements, i % 2 == 0 ? 0 : indices[i], i);
                    ++indices[i];
                    i = 0;
                    break;
                } else {
                    indices[i] = 0;
                    ++i;
                }
            }
            ++count;
            return elements;
        }
    }

    private static class SparsePermutationsGenerator<T> implements Iterator<T[]> {

        private final int permutations;
        private int count;
        private int i = -1;
        private final int n;
        private final T[] elements;
        private final int[] indices;
        private final int[] buckets;

        public SparsePermutationsGenerator(T[] elements, int[] buckets) {
            this.elements = elements;
            this.buckets = buckets;
            this.n = elements.length;
            indices = new int[n];
            permutations = factorial(n) / (Arrays.stream(buckets)
                    .map(PermutationsGenerator::factorial)
                    .reduce(1, (a,b) -> a*b));
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < permutations;
        }

        @Override
        public T[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            while(i < n) {
                if (i == -1) {
                    ++i;
                    break;
                } else if (indices[i] < i) {
                    swap(elements, i % 2 == 0 ? 0 : indices[i], i);
                    ++indices[i];
                    i = 0;
                    break;
                } else {
                    indices[i] = 0;
                    ++i;
                }
            }
            ++count;
            return elements;
        }
    }

    private static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    private static <T> void swap(T[] input, int a, int b) {
        T tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }
}
