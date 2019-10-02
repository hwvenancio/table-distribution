package br.wedding.loader;

import br.wedding.Guest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class CsvGuestLoader implements GuestLoader {

    private final Supplier<Reader> reader;

    public CsvGuestLoader(File file) {
        reader = () -> {
            try {
                return new FileReader(file);
            } catch (FileNotFoundException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    @Override
    public Guest[] load() {
        try {
            CSVParser csv = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader.get());
            List<String> tagNames = csv.getHeaderMap().entrySet().stream().filter(entry -> entry.getValue() != 0).map(Map.Entry::getKey).collect(toList());
            return StreamSupport.stream(csv.spliterator(), false)
                    .map(record -> {
                        Guest.GuestBuilder guest = Guest.builder().withName(record.get(0));
                        tagNames.forEach(tagName -> {
                            String[] tagValues = record.get(tagName).split(",");
                            for (String tagValue : tagValues) {
                                guest.tag(tagName, tagValue);
                            }
                        });

                        return guest.build();
                    })
                    .toArray(Guest[]::new);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
