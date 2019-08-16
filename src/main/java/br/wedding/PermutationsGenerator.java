package br.wedding;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static br.wedding.Utils.factorial;
import static br.wedding.Utils.swap;

public class PermutationsGenerator {

    public static <T> Iterator<T[]> full(T[] elements) {
        return new FullPermutationsGenerator<>(elements);
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
                    int swapIndex1 = i % 2 == 0 ? 0 : indices[i];
                    int swapIndex2 = i;
                    swap(elements, swapIndex1, swapIndex2);
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


}
