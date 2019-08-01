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
}
