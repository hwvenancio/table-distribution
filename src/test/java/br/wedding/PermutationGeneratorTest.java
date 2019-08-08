package br.wedding;


import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class PermutationGeneratorTest {

    @Test
    public void fullPermutations_noExtras1() {
        String[] guests = new String[] {"1"};

        List<String> allCombinations = stream(PermutationsGenerator.full(guests))
                .map(permutation -> String.join(",", permutation))
                .collect(toList());

        assertThat(allCombinations)
                .hasSize(1)
                .containsExactlyInAnyOrder(
                        "1"
                );
    }

    @Test
    public void fullPermutations_noExtras2() {
        String[] guests = new String[] {"1", "2"};

        List<String> allCombinations = stream(PermutationsGenerator.full(guests))
                .map(permutation -> String.join(",", permutation))
                .collect(toList());

        assertThat(allCombinations)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        "1,2"
                        , "2,1"
                );
    }

    @Test
    public void fullPermutations_noExtras3() {
        String[] guests = new String[] {"1", "2", "3"};

        List<String> allCombinations = stream(PermutationsGenerator.full(guests))
                .map(permutation -> String.join(",", permutation))
                .collect(toList());

        assertThat(allCombinations)
                .hasSize(3*2)
                .containsExactlyInAnyOrder(
                        "1,2,3"
                        , "2,1,3"
                        , "3,1,2"
                        , "1,3,2"
                        , "2,3,1"
                        , "3,2,1"
                );
    }

    @Test
    public void fullPermutations_noExtras4() {
        String[] guests = new String[] {"1", "2", "3", "4"};

        List<String> allCombinations = stream(PermutationsGenerator.full(guests))
                .map(permutation -> String.join(",", permutation))
                .collect(toList());

        assertThat(allCombinations)
                .hasSize(4*3*2);
    }

    @Test
    public void oneTable_sparsePermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4"};

        // when
        List<String> combinations = stream(PermutationsGenerator.sparse(guests, new int[] {4}))
                .map(all ->
                        Arrays.stream(Arrays.copyOfRange(all, 0, 4)).sorted()
                                .collect(joining(","))
                )
                .collect(toList());

        // then
        assertThat(combinations)
                .hasSize(1)
                .containsExactlyInAnyOrder(
                        "1,2,3,4"
                );
    }

    @Test
    public void twoTables_sparsePermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4"};

        // when
        List<String> combinations = stream(PermutationsGenerator.sparse(guests, new int[] {2, 2}))
                .map(all ->
                        Stream.concat(
                                Arrays.stream(Arrays.copyOfRange(all, 0, 2)).sorted()
                                , Arrays.stream(Arrays.copyOfRange(all, 2, 4)).sorted()
                        ).collect(joining(","))
                )
                .collect(toList());

        // then
        assertThat(combinations)
                .hasSize(6)
                .containsExactlyInAnyOrder(
                        "1,2,3,4"
                        , "1,3,2,4"
                        , "1,4,2,3"
                        , "2,3,1,4"
                        , "2,4,1,3"
                        , "3,4,1,2"
                );
    }

    @Test
    public void threeTables_sparsePermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        // when
        List<String[]> allCombinations = stream(PermutationsGenerator.sparse(guests, new int[] {3, 3, 3}))
                .map(permutation -> Arrays.copyOf(permutation, permutation.length))
                .collect(toList());

        // then
        Map<String, Long> tableRepetitions = allCombinations.stream()
                .map(all ->
                        Stream.concat(Stream.concat(
                                Arrays.stream(Arrays.copyOfRange(all, 0, 3)).sorted()
                                , Arrays.stream(Arrays.copyOfRange(all, 3, 6)).sorted())
                                , Arrays.stream(Arrays.copyOfRange(all, 6, 9)).sorted())
                                .collect(joining(","))
                )
                .collect(groupingBy(Function.identity(), counting()));

        assertThat(tableRepetitions)
                .allSatisfy((key, value) ->
                        assertThat(value)
                                .as("Found %d repetitions of %s", value, key)
                                .isEqualTo(1L));
    }

    private static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, 0)
                , false);
    }
}
