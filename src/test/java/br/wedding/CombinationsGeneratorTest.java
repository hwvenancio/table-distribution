package br.wedding;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static br.wedding.TestUtils.stream;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class CombinationsGeneratorTest {

    @Test
    public void oneTable_groupedPermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4"};

        // when
        List<String> combinations = stream(CombinationsGenerator.grouped(guests, new int[] {4}))
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
    public void twoTables_groupedPermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4"};

        // when
        List<String> combinations = stream(CombinationsGenerator.grouped(guests, new int[] {2, 2}))
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
    public void threeTables_groupedPermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        // when
        List<String[]> allCombinations = stream(CombinationsGenerator.grouped(guests, new int[] {3, 3, 3}))
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

    @Test
    public void twoUnevenTables_groupedPermutations_noInTablePermutations() {
        // given
        String[] guests = new String[] { "1", "2", "3", "4", "5"};

        // when
        List<String> combinations = stream(CombinationsGenerator.grouped(guests, new int[] {2, 3}))
                .map(all ->
                        Stream.concat(
                                Arrays.stream(Arrays.copyOfRange(all, 0, 2)).sorted()
                                , Arrays.stream(Arrays.copyOfRange(all, 2, 5)).sorted()
                        ).collect(joining(","))
                )
                .collect(toList());

        // then
        assertThat(combinations)
                .hasSize(10)
                .containsExactlyInAnyOrder(
                        "1,2,3,4,5"
                        , "1,3,2,4,5"
                        , "1,4,2,3,5"
                        , "1,5,2,3,4"
                        , "2,3,1,4,5"
                        , "2,4,1,3,5"
                        , "2,5,1,3,4"
                        , "3,4,1,2,5"
                        , "3,5,1,2,4"
                        , "4,5,1,2,3"
                );
    }
}