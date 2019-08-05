package br.wedding;

public class BruteForceStrategy extends BaseBruteForceStrategy {

    public BruteForceStrategy() {
        super((guests, tables) -> PermutationsGenerator.full(guests));
    }

}
