package br.wedding;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DistributionTest {

    @Test
    public void singleTable_maxGuests() {
        // given
        Guest[] guests = givenGuests(
                name("A")
                , name("B")
                , name("C")
                , name("D")
                , name("E")
                , name("F")
                , name("G")
                , name("H")
                , name("I")
                , name("J")
        );
        Table[] tables = givenTables(
                capacity(10)
        );

        // when
        Result result = Distribution.calculate(guests, tables);

        // then
        assertThat(result.tables[0].count()).isEqualTo(10);
    }

    @Test
    public void twoTables_guestsWithOppositeTags() {
        // given
        Guest[] guests = givenGuests(
                name("A").tag("family", "groom")
                , name("B").tag("family", "groom")
                , name("C").tag("family", "groom")
                , name("D").tag("family", "groom")
                , name("E").tag("family", "bride")
                , name("F").tag("family", "bride")
                , name("G").tag("family", "bride")
                , name("H").tag("family", "bride")
        );
        Table[] tables = givenTables(
                capacity(4)
                , capacity(4)
        );

        // when
        Result result = Distribution.of(guests, tables).sortByTag("family").calculate();

        // then
        assertThat(result.tables[0].count()).isEqualTo(4);
        assertThat(result.tables[1].count()).isEqualTo(4);

        MultiValuedMap<String, String> familyBrideTag = new HashSetValuedHashMap<>();
        familyBrideTag.put("family", "bride");
        MultiValuedMap<String, String> familyGroomTag = new HashSetValuedHashMap<>();
        familyGroomTag.put("family", "groom");
        assertThat(result.tables[0].guests)
                .isNotEmpty()
                .allSatisfy(guest ->
                        assertThat(guest.tags).isEqualTo(familyBrideTag));
        assertThat(result.tables[1].guests)
                .isNotEmpty()
                .allSatisfy(guest ->
                        assertThat(guest.tags).isEqualTo(familyGroomTag));
    }

    private static Guest[] givenGuests(GuestBuilder... builders) {
        return Arrays.stream(builders)
                .map(GuestBuilder::builder)
                .toArray(Guest[]::new);
    }

    private static Table[] givenTables(TableBuilder... builders) {
        return Arrays.stream(builders)
                .map(TableBuilder::build)
                .toArray(Table[]::new);
    }

    static GuestBuilder name(String name) {
        return new GuestBuilder().withName(name);
    }

    static TableBuilder capacity(int capacity) {
        return new TableBuilder().withCapacity(capacity);
    }

    private static class GuestBuilder {

        private String name;
        private MultiValuedMap<String, String> tags = new HashSetValuedHashMap<>();

        public GuestBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public GuestBuilder tag(String name, String value) {
            this.tags.put(name, value);
            return this;
        }

        public Guest builder() {
            return new Guest(name, tags);
        }
    }

    private static class TableBuilder {

        private int capacity;

        public TableBuilder withCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Table build() {
            return new Table(capacity);
        }
    }
}
