package eu.janinko.andaria.spherescript.sphere.objects;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class Resource {
    private final long count;
    private final String defname;

    public Resource(long count, String defname) {
        this.count = count;
        this.defname = defname;
    }

    public long getCount() {
        return count;
    }

    public String getDefname() {
        return defname;
    }

    @Override
    public String toString() {
        return count + "x " + defname;
    }
    
}
