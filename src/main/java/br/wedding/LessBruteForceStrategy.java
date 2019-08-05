package br.wedding;

import java.util.Arrays;

public class LessBruteForceStrategy extends BaseBruteForceStrategy {

    public LessBruteForceStrategy() {
        super((guests, tables) -> PermutationsGenerator.sparse(guests, Arrays.stream(tables).mapToInt(t -> t.capacity).toArray()));
    }

}
