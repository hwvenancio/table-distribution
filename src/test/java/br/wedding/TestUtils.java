package br.wedding;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TestUtils {

    static Guest[] givenGuests(Guest.GuestBuilder... builders) {
        return Arrays.stream(builders)
                .map(Guest.GuestBuilder::build)
                .toArray(Guest[]::new);
    }

    static Table[] givenTables(Table.TableBuilder... builders) {
        return Arrays.stream(builders)
                .map(Table.TableBuilder::build)
                .toArray(Table[]::new);
    }

    static Guest.GuestBuilder name(String name) {
        return Guest.builder().withName(name);
    }

    static Table.TableBuilder capacity(int capacity) {
        return Table.builder().withCapacity(capacity);
    }

    static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, 0)
                , false);
    }
}
