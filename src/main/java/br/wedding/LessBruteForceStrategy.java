package br.wedding;

import java.util.Arrays;

public class LessBruteForceStrategy extends BaseBruteForceStrategy {

    public LessBruteForceStrategy() {
        super((guests, tables) -> CombinationsGenerator.grouped(guests, Arrays.stream(tables).mapToInt(t -> t.capacity).toArray()));
    }

}
