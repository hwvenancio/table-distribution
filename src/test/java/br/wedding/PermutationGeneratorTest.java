package br.wedding;


import org.junit.Test;

import java.util.List;

import static br.wedding.TestUtils.stream;
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

}
