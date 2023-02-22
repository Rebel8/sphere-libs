package eu.janinko.andaria.spherescript.sphere.objects;

import lombok.Data;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Data
public abstract class Def {
    
    protected String defname;

    public Def(String defname) {
        this.defname = defname;
    }

    public Def() {
    }
    
}
