package eu.janinko.andaria.spherescript.sphere.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Getter
public class Chardef extends Objectdef {
    private long armor;
    private long moverate;
    private long reslevel;
    private List<Resource> foodtype = Collections.emptyList();
    private String icon;
    private String sound;
    private List<String> desires = Collections.emptyList();
    private List<String> aversions = Collections.emptyList();
    private long anim;
    private List<String> speech = new ArrayList();
    
    public Chardef(final String defid) {
        super(defid);
    }

    public void setArmor(long armor) {
        this.armor = armor; 
    }

    public void setMoverate(long moverate) {
        this.moverate = moverate;
    }

    public void setReslevel(long reslevel) {
        this.reslevel = reslevel;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    

    public void addSpeech(String speech){
        this.speech.add(speech);
    }

    public void setFoodtype(List<Resource> foodtype) {
        this.foodtype = foodtype;
    }


    public void setDesires(List<String> desires) {
        this.desires = desires;
    }

    public void setAversions(List<String> aversions) {
        this.aversions = aversions;
    }

    public void setAnim(long anim) {
        this.anim = anim;
    }


    public List<String> getDesires() {
        return desires;
    }

    public List<String> getAversions() {
        return aversions;
    }

    @Override
    public String toString() {
        return "Chardef{" + "defname=" + defname + ", defid=" + defid + '}';
    }
    
}
