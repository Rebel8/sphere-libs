package eu.janinko.andaria.spherescript.sphere.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Getter
@ToString
public abstract class Objectdef extends Def implements Tagable{
    protected final String defid;
    protected String category;
    protected String subsection;
    protected String color;
    protected String description;
    protected String name;
    protected List<Resource> resources = null;
    protected String id;
    protected List<String> tevents = new ArrayList();
    protected Map<String, String> tags = new TreeMap<>();
    protected Long damageMax;
    protected Long damageMin;

    protected Objectdef(final String defid) {
        this.defid = defid;
        try{
            Integer.parseInt(defid, 16);
        }catch(NumberFormatException ex){
            defname = defid;
        }
    }
    
    private static final Pattern namePattern = Pattern.compile("%([^%/]+)(/([^%]+))?%");
    public String getNameSingular(){
        if(name == null) return null;
        Matcher matcher = namePattern.matcher(name);
        return matcher.replaceAll("$3");
    }
    public String getNamePlural(){
        if(name == null) return null;
        Matcher matcher = namePattern.matcher(name);
        return matcher.replaceAll("$1");
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public void addTevent(String tevent){
        tevents.add(tevent);
    }
    
    @Override
    public void addTag(String name, String value){
        tags.put(name, value);
    }
    
    @Override
    public String getTag(String name){
        return tags.get(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDamage(long damage) {
        this.damageMin = damage;
        this.damageMax = damage;
    }

    public void setDamage(long damageMin, long damageMax) {
        this.damageMin = damageMin;
        this.damageMax = damageMax;
    }
}
