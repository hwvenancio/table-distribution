package br.wedding;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Iterator;

public class PermutationsGenerator {

    public static <T> Iterator<T[]> full(T[] elements) {
        return new FullPermutationsGenerator<>(elements);
    }

    public static <T> Iterator<T[]> sparse(T[] elements, int[] buckets) {
        Validate.isTrue(elements.length == Arrays.stream(buckets).sum(), "Elements length must be equal buckets sum");
//        return new SparsePermutationsGenerator<>(elements, buckets);
        return new FullPermutationsGenerator<>(elements);
    }

    private static class FullPermutationsGenerator<T> implements Iterator<T[]> {

        private int i = 0;
        private final int n;
        private final T[] elements;
        private final int[] indices;

        public FullPermutationsGenerator(T[] elements) {
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

//    private static class SparsePermutationsGenerator<T> implements Iterator<T[]> {
//        public SparsePermutationsGenerator(T[] elements, int[] buckets) {
//
//
//        }
//    }
}
