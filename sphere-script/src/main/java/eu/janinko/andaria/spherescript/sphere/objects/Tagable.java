package eu.janinko.andaria.spherescript.sphere.objects;

import lombok.Setter;

import java.util.Map;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public interface Tagable {
    void addTag(String name, String value);

    String getTag(String name);

    Map<String, String> getTags();
}
