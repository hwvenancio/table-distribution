package br.wedding;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

public class Guest {

    public final String name;
    public final MultiValuedMap<String, String> tags;

    public Guest(String name, MultiValuedMap<String, String> tags) {
        this.name = name;
        this.tags = MultiMapUtils.unmodifiableMultiValuedMap(
                tags == null ? new HashSetValuedHashMap<>() : tags
        );
    }

    public static GuestBuilder builder() {
        return new GuestBuilder();
    }

    public static class GuestBuilder {

        private String name;
        private final MultiValuedMap<String, String> tags = new HashSetValuedHashMap<>();

        private GuestBuilder() { }

        public GuestBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public GuestBuilder tag(String name, String value) {
            this.tags.put(name, value);
            return this;
        }

        public Guest build() {
            return new Guest(name, tags);
        }
    }

}
