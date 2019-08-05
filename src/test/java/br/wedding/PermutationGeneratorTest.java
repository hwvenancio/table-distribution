package br.wedding;


import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class PermutationGeneratorTest {

    @Test
    public void threeTables_sparsePermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        // when
        Iterator<String[]> permutationGenerator = PermutationsGenerator.sparse(guests, new int[] {3, 3, 3});
        Iterable<String[]> iterable = () -> permutationGenerator;
        List<String[]> allCombinations = StreamSupport.stream(iterable.spliterator(), false)
                .map(permutation -> Arrays.copyOf(permutation, permutation.length))
                .collect(toList());

        // then
        Map<Set<String>, Long> firstTableRepetitions = allCombinations.stream()
                .map(all -> Arrays.stream(Arrays.copyOfRange(all, 0, 3)).collect(toSet()))
                .collect(groupingBy(Function.identity(), counting()));

        assertThat(firstTableRepetitions)
                .allSatisfy((key, value) -> {
                    assertThat(value)
                            .as("Found %d repetitions of %s", value, key)
                            .isEqualTo(1L);
                });
    }
}