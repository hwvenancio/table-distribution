package br.wedding;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.junit.Test;

import static br.wedding.TestUtils.capacity;
import static br.wedding.TestUtils.givenGuests;
import static br.wedding.TestUtils.givenTables;
import static br.wedding.TestUtils.name;
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
        result.print();
        assertThat(result.tables[0].count()).isEqualTo(10);
    }

    @Test
    public void twoTables_guestsWithOppositeTags() {
        // given
        Guest[] guests = givenGuests(
                name("A").tag("inviter", "groom")
                , name("B").tag("inviter", "groom")
                , name("C").tag("inviter", "groom")
                , name("D").tag("inviter", "groom")
                , name("E").tag("inviter", "bride")
                , name("F").tag("inviter", "bride")
                , name("G").tag("inviter", "bride")
                , name("H").tag("inviter", "bride")
        );
        Table[] tables = givenTables(
                capacity(4)
                , capacity(4)
        );

        // when
        Result result = Distribution.of(guests, tables).sortByTag("inviter").calculate();

        // then
        result.print();
        assertThat(result.tables[0].count()).isEqualTo(4);
        assertThat(result.tables[1].count()).isEqualTo(4);

        MultiValuedMap<String, String> brideTag = new HashSetValuedHashMap<>();
        brideTag.put("inviter", "bride");
        MultiValuedMap<String, String> groomTag = new HashSetValuedHashMap<>();
        groomTag.put("inviter", "groom");
        assertThat(result.tables[0].guests)
                .isNotEmpty()
                .allSatisfy(guest ->
                        assertThat(guest.tags).isEqualTo(brideTag));
        assertThat(result.tables[1].guests)
                .isNotEmpty()
                .allSatisfy(guest ->
                        assertThat(guest.tags).isEqualTo(groomTag));
    }

    @Test
    public void distributeFamilies_twoTablesNotFull() {
        Guest[] guests = givenGuests(
                name("A").tag("family", "tailor")
                , name("B").tag("family", "tailor")
                , name("C").tag("family", "tailor")
                , name("D").tag("family", "smith")
                , name("E").tag("family", "smith")
                , name("F").tag("family", "thatcher")
                , name("G").tag("family", "thatcher")
        );
        Table[] tables = givenTables(
                capacity(5)
                , capacity(5)
        );

        // when
        Result result = Distribution.of(guests, tables).sortByTag("family").calculate();

        // then
        result.print();
        assertThat(result.tables[0].count()).isEqualTo(4);
        assertThat(result.tables[1].count()).isEqualTo(3);

        MultiValuedMap<String, String> smithTag = new HashSetValuedHashMap<>();
        smithTag.put("family", "smith");
        MultiValuedMap<String, String> tailorTag = new HashSetValuedHashMap<>();
        tailorTag.put("family", "tailor");
        MultiValuedMap<String, String> thatcherTag = new HashSetValuedHashMap<>();
        thatcherTag.put("family", "thatcher");

        assertThat(result.tables[0].guests)
                .isNotEmpty()
                .allSatisfy(guest ->
                        assertThat(guest.tags).isIn(smithTag, thatcherTag));
        assertThat(result.tables[1].guests)
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(guest ->
                        assertThat(guest.tags).isEqualTo(tailorTag));
    }

}
