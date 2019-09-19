package br.wedding;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class JeneticsStrategy implements Distribution.Strategy {

    private final long limit;

    public JeneticsStrategy() {
        this(100);
    }

    public JeneticsStrategy(int limit) {
        this.limit = limit;
    }

    @Override
    public Stream<Table> calculate(Guest[] guests, Table[] tables) {
        Factory<Genotype<IntegerGene>> gtf = Genotype.of(
                IntegerChromosome.of(0, tables.length - 1, guests.length));

        Engine<IntegerGene, Integer> engine = Engine.builder(score(tables, guests), gtf)
                .build();

        Genotype<IntegerGene> result = engine.stream()
                .limit(limit)
                .collect(EvolutionResult.toBestGenotype());

        return fullyConvert(result, tables, guests);
    }

    private Stream<Table> fullyConvert(Genotype<IntegerGene> result, Table[] tables, Guest[] guests) {
        List<Set<Guest>> converted = convert(result, tables, guests);
        return IntStream.range(0, tables.length)
                .mapToObj(i -> {
                    Table t = tables[i];
                    Guest[] g = converted.get(i).toArray(new Guest[0]);
                    return new Table(t.capacity, g);
                });
    }

    private static Function<Genotype<IntegerGene>, Integer> score(Table[] tables, Guest[] guests) {
        return gt -> calculateScore(convert(gt, tables, guests));
    }

    private static List<Set<Guest>> convert(Genotype<IntegerGene> gt, Table[] tables, Guest[] guests) {
        List<Set<Guest>> distributed = IntStream.range(0, tables.length)
                .mapToObj(i -> (Set<Guest>) new HashSet<Guest>())
                .collect(toList());
        for (int i = 0; i < guests.length; ++i) {
            IntegerGene chromosome = gt.get(0, i);
            int index = chromosome.intValue();
            Set<Guest> table = distributed.get(index);
            if (table.size() < tables[index].capacity)
                table.add(guests[i]);
        }
        return distributed;
    }

    private static int calculateScore(List<Set<Guest>> tables) {
        int sum = 0;
        for (Set<Guest> table : tables) {
            int score = calculateScore(table);
            if (score == 0)
                return 0;
            sum += score;
        }
        return sum;
    }

    private static int calculateScore(Set<Guest> guests) {
        int emptySpaces = 0;

        Map<Map.Entry<String, String>, Long> matchingTags = guests.stream()
                .flatMap(g -> g.tags.entries().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return matchingTags.values().stream()
                .filter(v -> v > 1)
                .mapToInt(Long::intValue)
                .sum() - (emptySpaces * emptySpaces);
    }
}
