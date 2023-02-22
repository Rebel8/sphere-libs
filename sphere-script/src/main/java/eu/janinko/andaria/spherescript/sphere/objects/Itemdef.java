package eu.janinko.andaria.spherescript.sphere.objects;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Getter
@ToString(callSuper = true)
public class Itemdef extends Objectdef {
    @Setter
    private String type;
    private Long weight;
    @Setter
    private String layer;
    @Setter
    private String tdata1;
    @Setter
    private String tdata2;
    @Setter
    private String tdata3;
    @Setter
    private String tdata4;
    private Long value;
    private Boolean pile;
    private Boolean flip;
    private Boolean dye;
    private Boolean repair;
    private List<Resource> skillmake = null;
    @Setter
    private String dupeitem;

    private Long hitMin, hitMax;
    @Setter
    private List<String> dupelist = null;

    // eq
    private Long reqstr;

    // armor
    private Long armor;

    // weapon
    private Boolean twohands;
    private Long speed;
    @Setter
    private String skill;
    private Long rangeMin, rangeMax;

/*
    private String AMMOANIM;	// Overrides TDATA4 for bow/crossbow type weapons.
	private String AMMOANIMHUE;	// Sets the color of the effect when firing bow/crossbow type weapons.
	private String AMMOANIMRENDER;	// Sets the render mode of the effect when firing bow/crossbow type weapons.
	private String AMMOCONT;	// Sets the container UID or ID where to search for ammos for bow/crossbow type weapons.
	private String AMMOTYPE;	// Overrides TDATA3 for bow/crossbow type weapons.
	private String ARMOR min,max;	// Gets or sets the base protection that the armour will give.
	private String BASEID;	// Gets the defname of the item if set, otherwise the ID.
	private String CAN;	// Gets or sets attributes for the item. See can_i_flags in sphere_defs.scp.
	private String DISPID;	// Gets or sets the ID to display as to clients.
	private String DUPEITEM;	// Gets or sets the defname of the item that this is a duplicate/rotation of (see DUPELIST).
	private String DUPELIST;	// Gets or sets a comma-separated list of items that this item will cycle through when rotated.
	private String HEIGHT;	// Gets or sets the height of the item.
	private String INSTANCES;	// Returns the number of this item that exist in the world.
	private String ISARMOR;	// Gets whether or not the item is considered to be armour.
	private String ISWEAPON;	// Gets whether or not the item is considered to be a weapon.
	private String RANGE min, max;	// Gets or sets the range of the weapon.
	private String RANGEH;	// Gets the maximum range of the weapon.
	private String RANGEL;	// Gets the minimum range of the weapon.
	private String REPLICATE;	// Gets or sets whether or not multiple instances of the item can be crafted at once.
	private String RESDISPDNHUE;	// Gets or sets the colour to display as to clients who don't have a high enough RESDISP to see the item.
	private String RESDISPDNID;	// Gets or sets the item ID to display as to clients who don't have a high enough RESDISP to see the item.
	private String RESLEVEL;	// Gets the minimum RESDISP required for a client to see the item.
	private String RESMAKE;	// Returns the names of the resources needed to craft the item.
	private String TFLAGS;	// Gets or sets the flags of the item from tiledata.mul.
*/

    public Itemdef(final String defid) {
        super(defid);
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void setReqstr(long reqstr) {
        this.reqstr = reqstr;
    }

    public void setTwohands(boolean twohands) {
        this.twohands = twohands;
    }

    public void setRepair(boolean repair) {
        this.repair = repair;
    }

    public void setPile(boolean pile) {
        this.pile = pile;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public void setDye(boolean dye) {
        this.dye = dye;
    }

    public void setArmor(long armor) {
        this.armor = armor;
    }

    public List<Resource> getSkillmake() {
        return skillmake;
    }

    public void setSkillmake(List<Resource> skillmake) {
        this.skillmake = skillmake;
    }

    public void setRange(long rangeMax) {
        this.rangeMin = 0L;
        this.rangeMax = rangeMax;
    }

    public void setRange(long rangeMin, long rangeMax) {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public void setHitpoints(long hitpoints) {
        this.hitMin = hitpoints;
        this.hitMax = hitpoints;
    }

    public void setHitpoints(long hitMin, long hitMax) {
        this.hitMin = hitMin;
        this.hitMax = hitMax;
    }

    public String getSpeedText(){
        if(speed == null) return null;
        if(speed >= 60) return "velmi vysoká";
        else if(speed >= 45) return "vysoká";
        else if(speed >= 30) return "průměrná";
        else if(speed >= 20) return "nízká";
        else return "velmi nízká";
    }

    public void inherit(Itemdef parent) {
        if (parent.getId() != null) {
            System.out.println("WARNING item " + getDefname() + "(" + getDefid() + ") has multiple predecesors.");
        }
        inherit(parent, Itemdef::getType, Itemdef::setType);
        inherit(parent, Itemdef::getWeight, Itemdef::setWeight);
        inherit(parent, Itemdef::getLayer, Itemdef::setLayer);
        inherit(parent, Itemdef::getValue, Itemdef::setValue);
        inherit(parent, Itemdef::getPile, Itemdef::setPile);
        inherit(parent, Itemdef::getFlip, Itemdef::setFlip);
        inherit(parent, Itemdef::getDye, Itemdef::setDye);
        inherit(parent, Itemdef::getRepair, Itemdef::setRepair);
        inherit(parent, Itemdef::getTdata1, Itemdef::setTdata1);
        inherit(parent, Itemdef::getTdata2, Itemdef::setTdata2);
        inherit(parent, Itemdef::getTdata3, Itemdef::setTdata3);
        inherit(parent, Itemdef::getTdata4, Itemdef::setTdata4);

        inherit(parent, Itemdef::getResources, Itemdef::setResources);
        inherit(parent, Itemdef::getSkillmake, Itemdef::setSkillmake);
        inherit(parent, Itemdef::getSpeed, Itemdef::setSpeed);
        inherit(parent, Itemdef::getReqstr, Itemdef::setReqstr);
        inherit(parent, Itemdef::getSkill, Itemdef::setSkill);
        inherit(parent, Itemdef::getTwohands, Itemdef::setTwohands);

        if(this.damageMin == null && this.damageMax == null){
            this.damageMin = parent.getDamageMin();
            this.damageMax = parent.getDamageMax();
        }

        if(this.hitMin == null && this.hitMax == null){
            this.hitMin = parent.getHitMin();
            this.hitMax = parent.getHitMax();
        }
    }

    private <T> void inherit(Itemdef parent, Function<Itemdef, T> getter, BiConsumer<Itemdef, T> setter) {
        if (getter.apply(this) == null && getter.apply(parent) != null) {
            setter.accept(this, getter.apply(parent));
        }
    }
}
